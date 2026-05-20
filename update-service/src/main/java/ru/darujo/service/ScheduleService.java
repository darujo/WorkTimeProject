package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.integration.ServiceType;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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
        executor.scheduleAtFixedRate(taskService.getBackUpTask(), 24, 24L * 86400, TimeUnit.HOURS);
        executor.scheduleAtFixedRate(taskService.getTaskAvailService(), getStartTime(23, 0), 2, TimeUnit.SECONDS);
//        addUpdate(new ArrayList<>());
    }

    private static Long getMilliSecondStartDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (System.currentTimeMillis() - c.getTimeInMillis());
    }

    private static final Long milliSecondStartDay = getMilliSecondStartDay();

    private Long getStartTime(Integer hour, Integer minute) {
        if (minute == null) {
            minute = 0;
        }
        if (minute < 0 || minute > 59) {
            throw new RuntimeException("Не верно заданы минуты");
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
