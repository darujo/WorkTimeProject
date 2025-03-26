package ru.darujo.convertor;

import ru.darujo.dto.WorkTimeDto;
import ru.darujo.model.WorkTime;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class WorkTimeBuilder {
    private Long id;
    private String nikName;
    private Float workTime;
    private Timestamp workDate;
    private Long taskId;
    private String comment;
    private Integer type;

    public WorkTimeBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public WorkTimeBuilder setNikName(String nikName) {
        this.nikName = nikName;
        return this;
    }

    public WorkTimeBuilder setWorkTime(Float workTime) {
        this.workTime = workTime;
        return this;
    }

    public WorkTimeBuilder setWorkDate(Timestamp workDate) {
        this.workDate = dateToStartTime(workDate);
        return this;
    }

    public WorkTimeBuilder setTaskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public WorkTimeBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public WorkTimeBuilder setType(Integer type) {
        this.type = type;
        return this;
    }

    public static WorkTimeBuilder createWorkTime() {
        return new WorkTimeBuilder();
    }


    public WorkTimeDto getWorkTimeDto() {
        return new WorkTimeDto(id, nikName, workDate, workTime, taskId, comment, type);
    }

    public WorkTime getWorkTime() {
        return new WorkTime(id, nikName, workDate, workTime, taskId, comment, type);
    }

    public Timestamp dateToStartTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }
}
