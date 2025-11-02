package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.user.UserInfoDto;
import ru.darujo.dto.workperiod.WorkUserFactPlan;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.WorkTimeServiceIntegration;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

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

    public Long getStartTime(Integer hour, Integer minute) {
        if (hour < 0 || hour > 23) {
            throw new RuntimeException("Не верно заданы часы");
        }
        if (minute < 0 || minute > 59) {
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

    private final Float PERCENT_WORK_TIME = 0.9f;

    public Runnable getAddWorkAvail() {
        return () -> {
            System.out.println("getAddWorkAvail");
            MessageType type = MessageType.AVAIL_WORK_LAST_DAY;

            List<UserInfoDto> users = messageInformationService.getUsersForMesType(type);
            if (users == null) {
                return;
            }
            users.forEach(userInfoDto -> {
                Timestamp date = calendarServiceIntegration.getLastWorkDay(userInfoDto.getNikName(), null, 2, false);
                WorkUserFactPlan workUserFactPlan = workTimeServiceIntegration.getUserWork(date, date, userInfoDto.getNikName(), "day");
                if (workUserFactPlan == null) {
                    return;
                }
                if (workUserFactPlan.getTimeFact() < workUserFactPlan.getTimePlan() * PERCENT_WORK_TIME) {
                    messageInformationService.addMessage(
                            new MessageInfoDto(
                                    userInfoDto,
                                    type,
                                    String.format("Вы не отметели работы за %S отмечено %S ч. по плану %S ч.", workUserFactPlan.getPeriodStr(), workUserFactPlan.getTimeFact(), workUserFactPlan.getTimePlan())));
                }
            });

        };
    }

    public Runnable getAddWorkAvailLastWeek() {
        return () -> {
            System.out.println("getAddWorkAvailLastWeek");
            MessageType type = MessageType.AVAIL_WORK_LAST_DAY;
            List<UserInfoDto> users = messageInformationService.getUsersForMesType(type);
            if (users == null) {
                return;
            }
            if (!calendarServiceIntegration.isDayAfterWeek(null, 2)) {
                return;
            }
            users.forEach(userInfoDto -> {
                Timestamp date = calendarServiceIntegration.getLastWorkDay(userInfoDto.getNikName(), null, 1, true);
                WorkUserFactPlan workUserFactPlan = workTimeServiceIntegration.getUserWork(date, date, userInfoDto.getNikName(), "week");
                if (workUserFactPlan.getTimeFact() < workUserFactPlan.getTimePlan() * PERCENT_WORK_TIME) {
                    messageInformationService.addMessage(
                            new MessageInfoDto(
                                    userInfoDto,
                                    type,
                                    String.format("Вы не отметели работы за %S отмечено %S ч. по плану %S ч.", workUserFactPlan.getPeriodStr(), workUserFactPlan.getTimeFact(), workUserFactPlan.getTimePlan())));
                }
            });

        };
    }

    public Runnable sendMessage() {
        return () -> messageInformationService.init();
    }


}
