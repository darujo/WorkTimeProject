package ru.darujo.model;

import lombok.Getter;
import ru.darujo.type.TypeEnum;

@Getter
public enum ServiceType implements TypeEnum {
    USER(10, "auth-service", 8183),
    INFORMATION(80, "information-service", 8188),
    WORK(10, "work-service", 8182),
    TASK(10, "task-service", 8181),
    WORK_TIME(10, "worktime-service", 8180),
    TELEGRAM(90, "telegram-service", 8187),
    CALENDAR(10, "calendar-service", 8184),
    RATE(10, "rate-service", 8186),
    FRONT(10, "front-service", 8185),
    GATE_WAY(10, "gateway-service", 5555);

    private final int priorityStop;
    private final String name;
    private final int port;

    ServiceType(int priorityStop, String name, int port) {
        this.priorityStop = priorityStop;
        this.name = name;
        this.port = port;
    }

}
