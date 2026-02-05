package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.ServiceType;

import java.io.File;
import java.time.ZonedDateTime;
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

        executor.scheduleAtFixedRate(taskService.getTaskAvailService(), 0, 2, TimeUnit.MINUTES);
//        addUpdate(new ArrayList<>());
    }


    public void addUpdate(ZonedDateTime timestamp, List<ServiceType> serviceTypeList, List<File> fileNameUpdates, String textUpdates) {
        executor.schedule(taskService.getTaskInfo(timestamp, " устнановлены обновления."), getStart(timestamp, 10L), TimeUnit.SECONDS);
        executor.schedule(taskService.getTask(fileNameUpdates, serviceTypeList, textUpdates), getStart(timestamp), TimeUnit.SECONDS);
    }

    private long getStart(ZonedDateTime timestamp) {
        return getStart(timestamp, 0L);
    }

    private long getStart(ZonedDateTime timestamp, Long minute) {
        long second = ChronoUnit.SECONDS.between(ZonedDateTime.now(), timestamp.minusMinutes(minute));
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
