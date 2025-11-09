package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
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
        executor.scheduleAtFixedRate(tasks.getAddWorkAvail(), tasks.getStartTime(11), 86400, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(tasks.getAddWorkAvailLastWeek(), tasks.getStartTime(12), 86400, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(tasks.sendMessage(), 0, 60, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(tasks.sendReportWorkFull(null, null), tasks.getStartTime(DayOfWeek.TUESDAY, 10), 86400 * 7, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(tasks.getMyVacationStart(), tasks.getStartTime( 20), 86400, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(tasks.getMyVacationEnd(), tasks.getStartTime( 14), 86400, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(tasks.getVacationEnd(), tasks.getStartTime( 12), 86400, TimeUnit.SECONDS);

    }

    public void sendWorkStatus(String author, Long chatId) {
        executor.schedule(tasks.sendReportWorkFull(author, chatId), 2, TimeUnit.SECONDS);
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

}
