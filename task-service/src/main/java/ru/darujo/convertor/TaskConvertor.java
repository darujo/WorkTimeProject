package ru.darujo.convertor;

import ru.darujo.dto.TaskDto;
import ru.darujo.model.Task;

public class TaskConvertor {

    public static TaskDto getTaskDto(Task task){
        return TaskBuilder
                .createWorkTime()
                .setId(task.getId())
                .setNikName(task.getNikName())
                .setCodeBTS(task.getCodeBTS())
                .setCodeDEVBO(task.getCodeDEVBO())
                .setDescription(task.getDescription())
                .setType(task.getType())
                .setWorkId(task.getWorkId ())
                .setTimeCreate(task.getTimeCreate())
                .getTaskDto();
    }
    public static Task getTask(TaskDto taskDto){
        return TaskBuilder
                .createWorkTime()
                .setId(taskDto.getId())
                .setNikName(taskDto.getNikName())
                .setCodeBTS(taskDto.getCodeBTS())
                .setCodeDEVBO(taskDto.getCodeDEVBO())
                .setDescription(taskDto.getDescription())
                .setType(taskDto.getType())
                .setWorkId(taskDto.getWorkId ())
                .setTimeCreate(taskDto.getTimeCreate())
                .getTask();
    }
}
