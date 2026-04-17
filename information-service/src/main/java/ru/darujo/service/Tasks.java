package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.project.ProjectDto;
import ru.darujo.dto.user.UserInfoDto;
import ru.darujo.dto.workperiod.WorkUserFactPlan;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.model.ChatInfo;
import ru.darujo.type.MessageType;
import ru.darujo.url.UrlWorkTime;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Tasks {
    MessageInformationService messageInformationService;
    private ReportService reportService;

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

    private UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    private final Float PERCENT_WORK_TIME = 0.9f;

    public RunnableNotException getAddWorkAvail(MessageType type) {
        return new RunnableNotException(
                () -> {
                    log.info("getAddWorkAvail");

                    Map<String, List<UserInfoDto>> users = messageInformationService.getUsersForMesType(type);
                    if (users == null) {
                        return;
                    }
                    users.forEach((nikName, userInfoDTOs) ->
                    {
                        try {
                            if (calendarServiceIntegration.isWorkDayUser(nikName, null)) {


                                Timestamp date = calendarServiceIntegration.getLastWorkDay(nikName,
                                        null,
                                        2,
                                        false);
                                WorkUserFactPlan workUserFactPlan = workTimeServiceIntegration.getUserWork(date, date, nikName, "day");
                                if (workUserFactPlan == null) {
                                    return;
                                }

                                if (workUserFactPlan.getTimeFact() < workUserFactPlan.getTimePlan() * PERCENT_WORK_TIME) {
                                    messageInformationService.addMessage(
                                            new MessageInfoDto(
                                                    type,
                                                    String.format("Вы не отметили работы за %S отмечено %S ч. по плану %S ч.",
                                                            workUserFactPlan.getPeriodStr(),
                                                            workUserFactPlan.getTimeFact(),
                                                            workUserFactPlan.getTimePlan())),
                                            userInfoDTOs);
                                }
                            }
                        } catch (ResourceNotFoundException e) {
                            log.error(e.getMessage());
                        }
                    });

                }
        );
    }

    public RunnableNotException getAddWorkAvailLastWeek(MessageType type) {
        return new RunnableNotException(() -> {
            log.info("getAddWorkAvailLastWeek");
            Map<String, List<UserInfoDto>> users = messageInformationService.getUsersForMesType(type);
            if (users == null) {
                return;
            }
            try {
                if (!calendarServiceIntegration.isDayAfterWeek(null, 2)) {
                    return;
                }

                users.forEach((nikName, userInfoDTos) -> {
                    Timestamp date;
                    date = calendarServiceIntegration.getLastWorkDay(nikName, null, 1, true);

                    WorkUserFactPlan workUserFactPlan = workTimeServiceIntegration.getUserWork(date, date, nikName, "week");
                    if (workUserFactPlan.getTimeFact() < workUserFactPlan.getTimePlan() * PERCENT_WORK_TIME) {
                        messageInformationService.addMessage(
                                new MessageInfoDto(
                                        type,
                                        String.format("Вы не отметили работы за %S отмечено %S ч. по плану %S ч.", workUserFactPlan.getPeriodStr(), workUserFactPlan.getTimeFact(), workUserFactPlan.getTimePlan())),
                                userInfoDTos);
                    }

                });
            } catch (ResourceNotFoundException e) {
                log.error(e.getMessage());

            }
        });
    }

    public RunnableNotException sendMessage() {
        return new RunnableNotException(() -> {
            log.info("Send message start");
            if (!messageInformationService.sendMesNotSend()) {
                ScheduleService.flagStartService = false;
                ScheduleService.getINSTANCE().sendMes();
            } else {
                ScheduleService.flagStartService = false;
            }
        });
    }

    private UserInfoDto getUserInfoDto(ChatInfo chatInfo) {
        if (chatInfo == null || chatInfo.getChatId() == null) {
            return null;
        }
        return new UserInfoDto(chatInfo.getSenderType().toString(), null, chatInfo.getAuthor(), chatInfo.getChatId(), chatInfo.getThreadId(), chatInfo.getOriginMessageId());
    }

    public RunnableNotException getMyVacationStart(MessageType type) {
        return new RunnableNotException(() -> {
            log.info("getMyVacationStart");
            Map<String, List<UserInfoDto>> users = messageInformationService.getUsersForMesType(type);
            if (users == null) {
                return;
            }
            users.forEach((nikName, userInfoDTOs) -> {
                try {
                    if (calendarServiceIntegration.isVacationStart(nikName, 1)) {
                        messageInformationService.addMessage(
                                new MessageInfoDto(
                                        type,
                                        "Ура с завтрашнего дня вы находитесь в отпуске. Не забудьте отключить телефон и насладиться тишиной."),
                                userInfoDTOs);
                    }
                } catch (ResourceNotFoundException e) {
                    log.error(e.getMessage());
                }
            });

        }
        );
    }

    public RunnableNotException getMyVacationEnd(MessageType type) {
        return new RunnableNotException(() -> {
            log.info("getMyVacationEnd");
            Map<String, List<UserInfoDto>> users = messageInformationService.getUsersForMesType(type);
            if (users == null) {
                return;
            }
            users.forEach((nikName, userInfoDTOs) -> {
                try {
                    if (calendarServiceIntegration.isVacationEnd(nikName)) {
                        messageInformationService.addMessage(
                                new MessageInfoDto(
                                        type,
                                        "К сожалению ваш отпуск подошел к концу и вам опять пора на работу. Не забудьте начать отмечать время прихода и ухода на работу"),
                                userInfoDTOs);
                    }
                } catch (ResourceNotFoundException e) {
                    log.error(e.getMessage());
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
                log.error(e.getMessage());
                return;
            }
            if (vacationDTOs == null) {
                log.error("Список отпусков пуст");
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
                                messageType,
                                vacationDTOs.get(0).getDateStartStr() + " начинается отпуск у сотрудников :\n" + users));
            }
        });
    }

    public RunnableNotException getSendReport(MessageType messageType, ChatInfo chatInfo) {
        return getSendReport(messageType, chatInfo, false);
    }

    public RunnableNotException getSendReport(MessageType messageType, ChatInfo chatInfo, boolean excel) {
        return new RunnableNotException(() -> {
            log.info("getSendReport {} {}", messageType, messageType.getName());

            sendFile(messageType, chatInfo, excel);

        });
    }

    private void sendFile(MessageType messageType, ChatInfo chatInfo, boolean excel) {
        String fileName = messageType.getName().replace(" ", "_");
        if (!messageType.isProject()) {
            sendFile(messageType,
                    chatInfo,
                    null,
                    reportService.print(
                            messageType,
                            null,
                            excel),
                    "Рассылка отчете \"" + messageType.getName() + "\" ",
                    fileName);

        } else {
            userServiceIntegration
                    .getProjects(null, null)
                    .forEach(projectDto ->
                            sendFile(messageType,
                                    chatInfo,
                                    projectDto,
                                    reportService.print(
                                            messageType,
                                            projectDto,
                                            excel),
                                    "Рассылка отчете \"" + messageType.getName() + "\" " + projectDto.getName(),
                                    fileName)
                    );
        }
    }

    private void sendFile(MessageType messageType, ChatInfo chatInfo, ProjectDto projectDto, byte[] report, String message, String fileName) {
        messageInformationService.sendFile(
                new MessageInfoDto(
                        chatInfo == null ? null : chatInfo.getAuthor(),
                        getUserInfoDto(chatInfo),
                        messageType,
                        message
                ),
                projectDto == null ? null : projectDto.getId(),
                fileName + "_" + (projectDto == null ? "" : (projectDto.getName() + "_")) + DateHelper.dateToISOStr(new Timestamp(System.currentTimeMillis())) + ".html",
                report);
    }

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }
}
