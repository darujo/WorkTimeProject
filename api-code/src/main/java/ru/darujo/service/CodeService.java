package ru.darujo.service;

import ru.darujo.model.TaskType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CodeService {
    Map<String, Map<Integer, TaskType>> codes = new HashMap<>();

    private CodeService() {
        initCode();
    }

    private void initCode() {
        codes.put("taskType", newTaskTypes());

    }

    private Map<Integer, TaskType> newTaskTypes() {
        Map<Integer, TaskType> code = new LinkedHashMap<>();
        code.put(1, new TaskType("ЗИ",true));
        code.put(5, new TaskType("Запросы по ЗИ",true));
        code.put(4, new TaskType("Изменение по ТЗ",true));
        code.put(2, new TaskType("Вендорные запросы",false));
        code.put(3, new TaskType("Админ",false));
        return code;
    }

    public static Map<Integer, TaskType> getTaskTypes() {
        return getInstance().codes.get("taskType");
    }

    public static String getTaskType(Integer code) {
        String codeName = getInstance().codes.get("taskType").get(code).getName();
        return Objects.requireNonNullElseGet(codeName, () -> "Не известный тип " + code);
    }

    public static Boolean getTaskTypeIsZi(Integer code) {
        return getInstance().codes.get("taskType").get(code).isZi();
    }

    private static CodeService obj;

    public static CodeService getInstance() {
        if (obj == null) {
            obj = new CodeService();
        }
        return obj;
    }

}
