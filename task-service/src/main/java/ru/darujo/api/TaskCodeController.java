package ru.darujo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.service.TaskStatus;
import ru.darujo.service.TaskType;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/v1/task/code")
public class TaskCodeController{

    @GetMapping("/type/{id}")
    public String getTaskType(@PathVariable Integer id) {
        return TaskType.getTaskTypeName(id);
    }

    @GetMapping("/type")
    public List<AttrDto<Integer>> getTaskTypes() {
        List<AttrDto<Integer>> attrDTOs = new ArrayList<>();
        TaskType.getTaskTypes().forEach((code) -> attrDTOs.add(new AttrDto<>(Integer.parseInt(code.getCode()), code.getName())));
        return attrDTOs;
    }

    @GetMapping("/status")
    public List<AttrDto<String>> getTaskStatusList() {
        List<AttrDto<String>> attrDTOs = new ArrayList<>();
        TaskStatus.getList().forEach((code) -> attrDTOs.add(new AttrDto<>(code.getCode(), code.getName())));
        return attrDTOs;
    }

}