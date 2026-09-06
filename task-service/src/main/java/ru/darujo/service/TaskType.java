package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.Code;

import java.util.List;

@Slf4j
@Service
public class TaskType {
    private static CodeTypeService codeTypeService;
    private static Code parent;

//    @PostConstruct
//    private void initCode() {
//        newTaskTypes();
//        for (int i = 0; i < 5; ) {
//            i++;
//            log.warn(getTaskTypeName(i));
//            log.warn(Boolean.toString(getTaskTypeIsZi(i)));
//
//        }
//
//    }

//    private void newTaskTypes() {
//        Map<Integer, ru.darujo.model.TaskType> code = new LinkedHashMap<>();
//        code.put(1, new ru.darujo.model.TaskType("ЗИ", true));
//        code.put(5, new ru.darujo.model.TaskType("Запросы по ЗИ", true));
//        code.put(4, new ru.darujo.model.TaskType("Изменение по ТЗ", true));
//        code.put(2, new ru.darujo.model.TaskType("Вендорные запросы", false));
//        code.put(6, new ru.darujo.model.TaskType("Улучшение", false));
//        code.put(3, new ru.darujo.model.TaskType("Админ", false));
//        return code;
//    }

    public static List<Code> getTaskTypes() {
        return codeTypeService.getCodes(parent, null).getContent();
    }

    public static String getTaskTypeName(Integer code) {
        return codeTypeService.findOne(parent, Integer.toString(code)).getName();
    }

    public static Boolean getTaskTypeIsZi(Integer code) {
        return Boolean.parseBoolean(codeTypeService.getAttrValue(parent, Integer.toString(code), "isZI"));
    }

    @Autowired
    public void setCodeTypeService(CodeTypeService codeTypeService) {
        TaskType.codeTypeService = codeTypeService;
        parent = codeTypeService.findOne(null, "taskType");
    }
}
