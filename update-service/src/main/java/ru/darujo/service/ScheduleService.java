package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.integration.ServiceType;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ScheduleService implements AutoCloseable {
    private TaskService taskService;

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

    @PostConstruct
    private void init() {
        executor.schedule(taskService.getBackUpTask(), 10, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(taskService.getBackUpTask(), 24, 24L * 86400, TimeUnit.HOURS);
        executor.scheduleAtFixedRate(taskService.getTaskAvailService(), DateHelper.getStartTime(23, 0), 2, TimeUnit.SECONDS);
//        addUpdate(new ArrayList<>());
    }

    public void addUpdate(LocalDateTime timestamp, List<ServiceType> serviceTypeList, List<File> fileNameUpdates, String textUpdates) {
        executor.schedule(taskService.getTaskInfo(timestamp, " установлены обновления."), getStart(timestamp, 10L), TimeUnit.SECONDS);
        executor.schedule(taskService.getTask(fileNameUpdates, serviceTypeList, textUpdates), getStart(timestamp), TimeUnit.SECONDS);
    }

    private long getStart(LocalDateTime timestamp) {
        return getStart(timestamp, 0L);
    }

    private long getStart(LocalDateTime timestamp, Long minute) {
        long second = ChronoUnit.SECONDS.between(LocalDateTime.now(), timestamp.minusMinutes(minute));
        return second < 0 ? 1L : second;
    }

    @Override
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

}
