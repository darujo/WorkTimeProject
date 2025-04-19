package ru.darujo.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CodeService {
    Map<String, Map<Integer, String>> codes = new HashMap<>();

    private CodeService() {
        initCode();
    }

    private void initCode() {
        codes.put("taskType", newTaskTypes());

    }

    private Map<Integer, String> newTaskTypes() {
        Map<Integer, String> code = new LinkedHashMap<>();
        code.put(1, "ЗИ");
        code.put(5, "Запросы по ЗИ");
        code.put(4, "Изменение по ТЗ");
        code.put(2, "Вендорные запросы");
        code.put(3, "Админ");
        return code;
    }

    public static Map<Integer, String> getTaskTypes() {
        return getInstance().codes.get("taskType");
    }

    public static String getTaskType(Integer code) {
        String codeName = getInstance().codes.get("taskType").get(code);
        return Objects.requireNonNullElseGet(codeName, () -> "Неизвестный код " + code);
    }

    private static CodeService obj;

    public static CodeService getInstance() {
        if (obj == null) {
            obj = new CodeService();
        }
        return obj;
    }

}
