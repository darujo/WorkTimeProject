package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.MessageType;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.ChatInfo;
import ru.darujo.type.ReportTypeDto;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ScheduleService {
    private Tasks tasks;

    @Autowired
    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

    @PostConstruct
    private void init() {
        executor.scheduleAtFixedRate(tasks.sendMessage(), 0, 60, TimeUnit.SECONDS);
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getPeriod() != null) {
                scheduleAtFixedRate(messageType);
            }
        }
    }

    private void scheduleAtFixedRate(MessageType messageType) {
        executor.scheduleAtFixedRate(
                getTask(messageType),
                messageType.getStartTime(),
                messageType.getPeriod(), TimeUnit.SECONDS);
    }

    public void sendReport(String reportTypeDto, ChatInfo chatInfo) {
        MessageType messageType;
        if (reportTypeDto.equals(ReportTypeDto.USER_WORK.toString())) {
            messageType = MessageType.WEEK_WORK_REPORT;
        } else if (reportTypeDto.equals(ReportTypeDto.ZI_STATUS.toString())) {
            messageType = MessageType.AVAIL_WORK_FULL_REPORT;
        } else if (reportTypeDto.equals(ReportTypeDto.ZI_WORK.toString())) {
            messageType = MessageType.ZI_WORK_REPORT;
        } else {
            throw new ResourceNotFoundRunTime("Нет такого типа отчета");

        }
        executor.schedule(getTask(messageType, chatInfo), 2, TimeUnit.SECONDS);
    }

    public void close() {
        // Корректное завершение
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            try {
                while (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    log.error("sto1");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
//        }));


    }

    private RunnableNotException getTask(MessageType messageType) {
        return getTask(messageType, null);
    }

    private RunnableNotException getTask(MessageType messageType, ChatInfo chatInfo) {
        if (messageType.equals(MessageType.AVAIL_WORK_LAST_DAY)) {
            return tasks.getAddWorkAvail(messageType);
        } else if (messageType.equals(MessageType.AVAIL_WORK_LAST_WEEK)) {
            return tasks.getAddWorkAvailLastWeek(messageType);
        } else if (messageType.equals(MessageType.AVAIL_WORK_FULL_REPORT)) {
            return tasks.sendReportWorkFull(messageType, chatInfo);
        } else if (messageType.equals(MessageType.VACATION_MY_START)) {
            return tasks.getMyVacationStart(messageType);
        } else if (messageType.equals(MessageType.VACATION_MY_END)) {
            return tasks.getMyVacationEnd(messageType);
        } else if (messageType.equals(MessageType.VACATION_USER_START)) {
            return tasks.getVacationStart(messageType);
        } else if (messageType.equals(MessageType.ZI_WORK_REPORT)) {
            return tasks.getZiWork(messageType, chatInfo);
        } else if (messageType.equals(MessageType.WEEK_WORK_REPORT)) {
            return tasks.getWeekWork(messageType, chatInfo);
        } else {
            throw new ResourceNotFoundRunTime("Нет такого типа отчета");
        }
    }

}
