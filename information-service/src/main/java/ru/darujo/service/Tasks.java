package ru.darujo.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.user.UserInfoDto;
import ru.darujo.dto.workperiod.WorkUserFactPlan;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.url.UrlWorkTime;

import java.sql.Timestamp;
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

    private TaskServiceIntegration taskServiceIntegration;

    @Autowired
    public void setTaskServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        this.taskServiceIntegration = taskServiceIntegration;
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



    private final Float PERCENT_WORK_TIME = 0.9f;

    public RunnableNotException getAddWorkAvail(MessageType type) {
        return new RunnableNotException(
                () -> {
                    log.info("getAddWorkAvail");

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

                }
        );
    }

    public RunnableNotException getAddWorkAvailLastWeek(MessageType type) {
        return new RunnableNotException(() -> {
            log.info("getAddWorkAvailLastWeek");
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
                    date = calendarServiceIntegration.getLastWorkDay(userInfoDto.getNikName(), null, 1, true);

                    WorkUserFactPlan workUserFactPlan = workTimeServiceIntegration.getUserWork(date, date, userInfoDto.getNikName(), "week");
                    if (workUserFactPlan.getTimeFact() < workUserFactPlan.getTimePlan() * PERCENT_WORK_TIME) {
                        messageInformationService.addMessage(
                                new MessageInfoDto(
                                        userInfoDto,
                                        type,
                                        String.format("Вы не отметили работы за %S отмечено %S ч. по плану %S ч.", workUserFactPlan.getPeriodStr(), workUserFactPlan.getTimeFact(), workUserFactPlan.getTimePlan())));
                    }

                });
            } catch (ResourceNotFoundException e) {
                log.error(e);

            }
        });
    }

    public RunnableNotException sendMessage() {
        return new RunnableNotException(() -> messageInformationService.init()) {
        };
    }

    public RunnableNotException sendReportWorkFull(MessageType messageType, String author, Long chatId) {
        return new RunnableNotException(() -> {
            log.info("sendReportWorkFull");
            LinkedList<String> sort = new LinkedList<>();
            sort.add("release");
            String report = htmlService.printRep(workServiceIntegration.getTimeWork(null, true, null, null, sort));
            messageInformationService.sendFile(new MessageInfoDto(author,
                    (chatId == null ? null : new UserInfoDto(null, author, chatId)),
                    messageType, "Рассылка отчете статус ЗИ"
            ), "Zi_Report_" + DataHelper.dateToISOStr(new Timestamp(System.currentTimeMillis())) + ".html", report);

        });
    }

    public RunnableNotException getMyVacationStart(MessageType type) {
        return new RunnableNotException(() -> {
            log.info("getMyVacationStart");
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

        }
        );
    }

    public RunnableNotException getMyVacationEnd(MessageType type) {
        return new RunnableNotException(() -> {
            log.info("getMyVacationEnd");
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
        });
    }

    public RunnableNotException getVacationStart(MessageType messageType) {
        return new RunnableNotException(() -> {
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
                            UrlWorkTime.getUrlVacation(vacationDto.getNikName(), vacationDto.getFirstName() + " " +
                                    vacationDto.getLastName()) + " ( " +
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
                                messageType,
                                vacationDTOs.get(0).getDateStartStr() + " начинается отпуск у сотрудников :\n" + users));
            }
        });
    }

    public RunnableNotException getZiWork(MessageType messageType, String author, Long chatId) {
        return new RunnableNotException(() -> {
            log.info("getZiWork");
            String report = getReportWork(true);
            messageInformationService.sendFile(new MessageInfoDto(author,
                    (chatId == null ? null : new UserInfoDto(null, author, chatId)),
                    messageType, "Факт загрузки по ЗИ"
            ), "ZI_Work_" + DataHelper.dateToISOStr(new Timestamp(System.currentTimeMillis())) + ".html", report);
        });
    }

    private String getReportWork(boolean ziSplit) {
        List<AttrDto<Integer>> taskListType = taskServiceIntegration.getTaskTypes();
        Timestamp date = calendarServiceIntegration.getLastWorkDay(null, null, 1, true);
        List<WorkUserTime> weekWorkList = workServiceIntegration.getWorkUserTime(ziSplit, date);
        return htmlService.getWeekWork(ziSplit, true, true, true, taskListType, weekWorkList);
    }

    public RunnableNotException getWeekWork(MessageType messageType, String author, Long chatId) {
        return new RunnableNotException(() -> {
            log.info("getWeekWork");
            String report = getReportWork(false);
            messageInformationService.sendFile(new MessageInfoDto(author,
                    (chatId == null ? null : new UserInfoDto(null, author, chatId)),
                    messageType, "Факт загрузки за предыдущую неделю"
            ), "Week_Work_" + DataHelper.dateToISOStr(new Timestamp(System.currentTimeMillis())) + ".html", report);
        });
    }

}
