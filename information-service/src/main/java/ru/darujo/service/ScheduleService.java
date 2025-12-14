package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.MessageType;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.type.ReportTypeDto;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
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

    public void sendReport(String reportTypeDto, String author, Long chatId, Integer threadId) {
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
        executor.schedule(getTask(messageType, author, chatId, threadId), 2, TimeUnit.SECONDS);
    }

    private void close() {
        // Корректное завершение
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }));


    }

    private RunnableNotException getTask(MessageType messageType) {
        return getTask(messageType, null, null, null);
    }

    private RunnableNotException getTask(MessageType messageType, String author, Long chatId, Integer threadId) {
        if (messageType.equals(MessageType.AVAIL_WORK_LAST_DAY)) {
            return tasks.getAddWorkAvail(messageType);
        } else if (messageType.equals(MessageType.AVAIL_WORK_LAST_WEEK)) {
            return tasks.getAddWorkAvailLastWeek(messageType);
        } else if (messageType.equals(MessageType.AVAIL_WORK_FULL_REPORT)) {
            return tasks.sendReportWorkFull(messageType, author, chatId, threadId);
        } else if (messageType.equals(MessageType.VACATION_MY_START)) {
            return tasks.getMyVacationStart(messageType);
        } else if (messageType.equals(MessageType.VACATION_MY_END)) {
            return tasks.getMyVacationEnd(messageType);
        } else if (messageType.equals(MessageType.VACATION_USER_START)) {
            return tasks.getVacationStart(messageType);
        } else if (messageType.equals(MessageType.ZI_WORK_REPORT)) {
            return tasks.getZiWork(messageType, author, chatId, threadId);
        } else if (messageType.equals(MessageType.WEEK_WORK_REPORT)) {
            return tasks.getWeekWork(messageType, author, chatId, threadId);
        } else {
            throw new ResourceNotFoundRunTime("Нет такого типа отчета");
        }
    }

}
