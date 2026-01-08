package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.ServiceType;

import java.util.Arrays;
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
    }

    public void addUpdate(List<String> fileNameUpdates) {
        addUpdate(fileNameUpdates, Arrays.stream(ServiceType.values()).toList());
    }

    public void addUpdate(List<String> fileNameUpdates, List<ServiceType> serviceTypeList) {
        executor.schedule(taskService.getTask(fileNameUpdates, serviceTypeList), 1, TimeUnit.MINUTES);
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
