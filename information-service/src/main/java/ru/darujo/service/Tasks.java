package ru.darujo.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.user.UserInfoDto;
import ru.darujo.dto.workperiod.WorkUserFactPlan;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.url.UrlWorkTime;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@Component
public class Tasks {
    MessageInformationService messageInformationService;

    @Autowired
    public void setMessageInformationService(MessageInformationService messageInformationService) {
        this.messageInformationService = messageInformationService;
    }

    private CalendarServiceIntegration calendarServiceIntegration;

    @Autowired
    public void setCalendarServiceIntegration(CalendarServiceIntegration calendarServiceIntegration) {
        this.calendarServiceIntegration = calendarServiceIntegration;
    }

    private WorkTimeServiceIntegration workTimeServiceIntegration;

    @Autowired
    public void setWorkTimeServiceIntegration(WorkTimeServiceIntegration workTimeServiceIntegration) {
        this.workTimeServiceIntegration = workTimeServiceIntegration;
    }

    private WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }

    private HtmlService htmlService;

    @Autowired
    public void setHtmlService(HtmlService htmlService) {
        this.htmlService = htmlService;
    }

    public Long getStartTime(Integer hour) {
        return getStartTime(hour, 0);
    }

    private final Long milliSecondStartDay;

    public Tasks() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        milliSecondStartDay = (System.currentTimeMillis() - c.getTimeInMillis());
    }

    public Long getStartTime(DayOfWeek dayOfWeek, Integer hour) {
        return getStartTime(dayOfWeek, hour, 0);
    }

    public Long getStartTime(Integer hour, Integer minute) {
        if (minute < 0 || minute > 59) {
            throw new RuntimeException("Не верно заданы часы");
        }
        if (hour < 0 || hour > 23) {
            throw new RuntimeException("Не верно заданы часы");
        }
        int millisecond = ((hour * 60) + minute) * 60 * 1000;
        long startTime = millisecond - milliSecondStartDay;
        startTime = startTime / 1000;
        if (startTime < 0) {
            startTime = startTime + 24 * 60 * 60;
        }
        return startTime;
    }

    public Long getStartTime(DayOfWeek dayOfWeek, Integer hour, Integer minute) {
        if (hour < 0 || hour > 23) {
            throw new RuntimeException("Не верно заданы часы");
        }
        if (minute < 0 || minute > 59) {
            throw new RuntimeException("Не верно заданы часы");
        }
        int millisecond = ((hour * 60) + minute) * 60 * 1000;
        long startTime = millisecond - milliSecondStartDay;
        int days = dayOfWeek.getValue() - LocalDate.now().getDayOfWeek().getValue();
        if (days < 0) {
            days = days + 7;
        }
        startTime = startTime / 1000;
        startTime = startTime + days * 86400L;
        if (startTime < 0) {
            startTime = startTime + 7 * 24 * 60 * 60;
        }
        log.info(dayOfWeek);
        log.info(hour);
        log.info(minute);
        log.info(startTime);
        return startTime;
    }

    private final Float PERCENT_WORK_TIME = 0.9f;

    public Runnable getAddWorkAvail() {
        return () -> {
            log.info("getAddWorkAvail");
            MessageType type = MessageType.AVAIL_WORK_LAST_DAY;

            List<UserInfoDto> users = messageInformationService.getUsersForMesType(type);
            if (users == null) {
                return;
            }
            users.forEach(userInfoDto -> {
                try {
                    if (calendarServiceIntegration.isWorkDayUser(userInfoDto.getNikName(), null)) {


                        Timestamp date = calendarServiceIntegration.getLastWorkDay(userInfoDto.getNikName(),
                                null,
                                2,
                                false);
                        WorkUserFactPlan workUserFactPlan = workTimeServiceIntegration.getUserWork(date, date, userInfoDto.getNikName(), "day");
                        if (workUserFactPlan == null) {
                            return;
                        }
                        if (workUserFactPlan.getTimeFact() < workUserFactPlan.getTimePlan() * PERCENT_WORK_TIME) {
                            messageInformationService.addMessage(
                                    new MessageInfoDto(
                                            userInfoDto,
                                            type,
                                            String.format("Вы не отметили работы за %S отмечено %S ч. по плану %S ч.", workUserFactPlan.getPeriodStr(), workUserFactPlan.getTimeFact(), workUserFactPlan.getTimePlan())));
                        }
                    }
                } catch (ResourceNotFoundException e) {
                    log.error(e);
                }
            });

        };
    }

    public Runnable getAddWorkAvailLastWeek() {
        return () -> {
            log.info("getAddWorkAvailLastWeek");
            MessageType type = MessageType.AVAIL_WORK_LAST_WEEK;
            List<UserInfoDto> users = messageInformationService.getUsersForMesType(type);
            if (users == null) {
                return;
            }
            try {
                if (!calendarServiceIntegration.isDayAfterWeek(null, 2)) {
                    return;
                }

                users.forEach(userInfoDto -> {
                    Timestamp date;
                    try {
                        date = calendarServiceIntegration.getLastWorkDay(userInfoDto.getNikName(), null, 1, true);

                        WorkUserFactPlan workUserFactPlan = workTimeServiceIntegration.getUserWork(date, date, userInfoDto.getNikName(), "week");
                        if (workUserFactPlan.getTimeFact() < workUserFactPlan.getTimePlan() * PERCENT_WORK_TIME) {
                            messageInformationService.addMessage(
                                    new MessageInfoDto(
                                            userInfoDto,
                                            type,
                                            String.format("Вы не отметили работы за %S отмечено %S ч. по плану %S ч.", workUserFactPlan.getPeriodStr(), workUserFactPlan.getTimeFact(), workUserFactPlan.getTimePlan())));
                        }
                    } catch (ResourceNotFoundException e) {
                        log.error(e);
                    }
                });
            } catch (ResourceNotFoundException e) {
                log.error(e);

            }
        };
    }

    public Runnable sendMessage() {
        return () -> messageInformationService.init();
    }

    public Runnable sendReportWorkFull(String author, Long chatId) {
        return () -> {
            log.info("sendReportWorkFull");
            LinkedList<String> sort = new LinkedList<>();
            sort.add("release");
            String report = htmlService.printRep(workServiceIntegration.getTimeWork(null, true, null, null, sort));
            messageInformationService.sendFile(new MessageInfoDto(author,
                    (chatId == null ? null : new UserInfoDto(null, author, chatId)),
                    MessageType.AVAIL_WORK_FULL_REPORT, "Рассылка отчете статус ЗИ"
            ), "Zi_Report_" + DataHelper.dateToYYYYMMDD(new Timestamp(System.currentTimeMillis())) + ".html", report);

        };
    }

    public Runnable getMyVacationStart() {
        return () -> {
            log.info("getMyVacationStart");
            MessageType type = MessageType.VACATION_MY_START;
            List<UserInfoDto> users = messageInformationService.getUsersForMesType(type);
            if (users == null) {
                return;
            }
            users.forEach(userInfoDto -> {
                try {
                    if (calendarServiceIntegration.isVacationStart(userInfoDto.getNikName(), 1)) {
                        messageInformationService.addMessage(
                                new MessageInfoDto(
                                        userInfoDto,
                                        type,
                                        "Ура с завтрашнего дня вы находитесь в отпуске. Не забудьте отключить телефон и насладиться тишеной."));
                    }
                } catch (ResourceNotFoundException e) {
                    log.error(e);
                }
            });

        };
    }

    public Runnable getMyVacationEnd() {
        return () -> {
            log.info("getMyVacationEnd");
            MessageType type = MessageType.VACATION_MY_END;
            List<UserInfoDto> users = messageInformationService.getUsersForMesType(type);
            if (users == null) {
                return;
            }
            users.forEach(userInfoDto -> {
                try {
                    if (calendarServiceIntegration.isVacationEnd(userInfoDto.getNikName())) {
                        messageInformationService.addMessage(
                                new MessageInfoDto(
                                        userInfoDto,
                                        type,
                                        "К сожалению ваш отпуск подошел к концу и вам опять пора на работу. Не забудьте начать отмечать время прихода и ухода на работу"));
                    }
                } catch (ResourceNotFoundException e) {
                    log.error(e);
                }
            });
        };
    }

    public Runnable getVacationEnd() {
        return () -> {
            log.info("getVacationEnd");
            StringBuffer users = new StringBuffer();
            List<VacationDto> vacationDTOs;
            try {
                vacationDTOs = calendarServiceIntegration.userVacationStart(null, 7);
            } catch (ResourceNotFoundException e) {
                log.error(e);
                return;
            }
            if (vacationDTOs == null) {
                log.error("vacationDTOs == null");
                return;
            }
            vacationDTOs.stream()
                    .map(vacationDto ->
                            UrlWorkTime.getUrlVacation( vacationDto.getNikName(),vacationDto.getFirstName() + " " +
                                    vacationDto.getLastName() ) + " ( " +
                                    vacationDto.getDays() + " дней последний день отпуска " +
                                    vacationDto.getDateEndStr() + " )")
                    .forEach(s -> {
                        if (users.isEmpty()) {
                            users.append(s);
                        } else {
                            users.append("\n").append(s);
                        }
                    });
            if (!vacationDTOs.isEmpty()) {
                messageInformationService.addMessage(
                        new MessageInfoDto(
                                null,
                                MessageType.VACATION_USER_START,
                                vacationDTOs.get(0).getDateStartStr() + " начинается отпуск у сотрудников :\n" + users));
            }
        };
    }
}
