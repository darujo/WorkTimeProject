package ru.darujo.api;

import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.service.CodeService;

import java.util.*;

@RestController()
@RequestMapping("/v1/task/code")
public class TaskCodeController{

    @GetMapping("/type/{id}")
    public String getTaskType(@PathVariable Integer id) {
        return CodeService.getTaskType(id);
    }

    @GetMapping("/type")
    public List<AttrDto<Integer>> getTaskTypes() {
        List<AttrDto<Integer>> attrDTOs = new ArrayList<>();
        CodeService.getTaskTypes().forEach((typeCode, type ) -> attrDTOs.add(new AttrDto<>(typeCode,type.getName())));
        return attrDTOs;
    }

}