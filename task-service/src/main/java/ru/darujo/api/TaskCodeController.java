package ru.darujo.api;

import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.parsing.DateParser;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.service.CodeService;

import java.util.*;

@RestController()
@RequestMapping("/v1/task/code")
public class TaskCodeController extends DateParser {

    @GetMapping("/type/{id}")
    public String getTaskType(@PathVariable Integer id) {
        return CodeService.getTaskType(id);
    }

    @GetMapping("/type")
    public List<AttrDto<Integer>> getTaskTypes() {
        List<AttrDto<Integer>> attrDTOs = new ArrayList<>();
        CodeService.getTaskTypes().forEach((type, name) -> attrDTOs.add(new AttrDto<>(type,name)));
        return attrDTOs;
    }

}