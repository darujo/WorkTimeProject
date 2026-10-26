package ru.darujo.dto.workrep;

import ru.darujo.dto.TaskDto;
import ru.darujo.dto.content.ContentElement;
import ru.darujo.dto.content.ContentObjectInterface;
import ru.darujo.dto.workperiod.UserWorkDto;

import java.util.List;

public class TaskContent extends TaskDto implements ContentObjectInterface {
    private List<UserWorkDto> content;

    public TaskContent(TaskDto taskDto, List<UserWorkDto> userWorkDtoList) {
        super(taskDto.getId(),
                taskDto.getNikName(),
                taskDto.getAuthorFirstName(),
                taskDto.getAuthorLastName(),
                taskDto.getAuthorPatronymic(),
                taskDto.getCodeBTS(),
                taskDto.getCodeDEVBO(),
                taskDto.getDescription(),
                taskDto.getType(),
                taskDto.getTypeStr(),
                taskDto.getWorkId(),
                taskDto.getTimeCreate(),
                taskDto.getExecutor(),
                taskDto.getAnalyst(),
                taskDto.getDeveloper(),
                taskDto.getTester(),
                taskDto.getStatus());
        this.content = userWorkDtoList;
    }

    @SuppressWarnings("unused")
    public TaskContent() {
    }

    @Override
    public int getCount() {
        return content.stream().mapToInt(ContentElement::getCount).sum();
    }

    @SuppressWarnings("unused")
    public List<?> getContent() {
        return content;
    }
}
