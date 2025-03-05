package ru.darujo.convertor;

import ru.darujo.dto.TaskDto;
import ru.darujo.model.Task;

public class TaskBuilder {
    private Long id;
    private String nikName;
    // № запроса (BTS)
    private String codeBTS;
    // № внутренней задачи (DEVBO)
    private String codeDEVBO;
    // Краткое описание ошибки
    private String description;
    // Тип задачи
    private  Integer type;
    // № ЗИ (ZI)
    private  Long workId;

    public TaskBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public TaskBuilder setNikName(String nikName) {
        this.nikName = nikName;
        return this;
    }

    public TaskBuilder setCodeBTS(String codeBTS) {
        this.codeBTS = codeBTS;
        return this;
    }

    public TaskBuilder setCodeDEVBO(String codeDEVBO) {
        this.codeDEVBO = codeDEVBO;
        return this;
    }

    public TaskBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder setType(Integer type) {
        this.type = type;
        return this;
    }

    public TaskBuilder setWorkId(Long workId) {
        this.workId = workId;
        return this;
    }

    public static TaskBuilder createWorkTime () {
        return new TaskBuilder();
    }
    public TaskDto getTaskDto(){
        return new TaskDto(id,
                nikName,
                codeBTS,
                codeDEVBO,
                description,
                type,
                workId);
    }
    public Task getTask(){
        return new Task(
                id,
                nikName,
                codeBTS,
                codeDEVBO,
                description,
                type,
                workId);
    }
}
