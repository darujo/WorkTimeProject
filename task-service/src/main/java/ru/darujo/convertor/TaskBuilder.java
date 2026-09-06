package ru.darujo.convertor;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.TaskDto;
import ru.darujo.model.Task;
import ru.darujo.service.TaskType;

import java.time.LocalDateTime;

public class TaskBuilder {

    private Long id;
    private String nikName;
    // № запроса (BTS)
    private String codeBTS;
    // № внутренней задачи (D E V B O)
    private String codeDEVBO;
    // Краткое описание ошибки
    private String description;
    // Тип задачи
    private Integer type;
    // № ЗИ (ZI)
    private Long workId;
    private LocalDateTime timeCreate;
    private Long projectId;
    private String executor;
    private String analyst;
    private String developer;
    private String tester;
    private String status;

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

    public TaskBuilder setTimeCreate(LocalDateTime timeCreate) {
        this.timeCreate = timeCreate;
        return this;
    }

    public static TaskBuilder createWorkTime() {
        return new TaskBuilder();
    }

    public TaskBuilder setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public TaskBuilder setExecutor(String executor) {
        this.executor = executor;
        return this;
    }

    public TaskBuilder setAnalyst(String analyst) {
        this.analyst = analyst;
        return this;
    }

    public TaskBuilder setDeveloper(String developer) {
        this.developer = developer;
        return this;
    }

    public TaskBuilder setTester(String tester) {
        this.tester = tester;
        return this;
    }

    public TaskBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public TaskDto getTaskDto() {
        return new TaskDto(id,
                nikName,
                null,
                null,
                null,
                codeBTS,
                codeDEVBO,
                description,
                type,
                TaskType.getTaskTypeName(type),
                workId,
                DateHelper.getZDT(timeCreate),
                executor,
                analyst,
                developer,
                tester,
                status);
    }

    public Task getTask() {
        return new Task(
                id,
                nikName,
                codeBTS,
                codeDEVBO,
                description,
                type,
                workId,
                null,
                timeCreate,
                projectId,
                executor,
                analyst,
                developer,
                tester,
                status);
    }
}
