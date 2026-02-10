package ru.darujo.service;

import ru.darujo.model.WorkTimeType;

import java.util.HashMap;
import java.util.Map;

public class WorkTimeTypeService {
    Map<Long, WorkTimeType> workTimeTypeMap = new HashMap<>();
    WorkTimeType admin;

    public void init() {
        workTimeTypeMap.put(1L, new WorkTimeType(null, 1L, "Разработка", true));
        workTimeTypeMap.put(2L, new WorkTimeType(null, 2L, "Консультация", false));
        workTimeTypeMap.put(3L, new WorkTimeType(null, 3L, "Анализ", false));
        workTimeTypeMap.put(4L, new WorkTimeType(null, 4L, "Тестирование", false));
        workTimeTypeMap.put(5L, new WorkTimeType(null, 5L, "Анализ ошибки", false));
        workTimeTypeMap.put(6L, new WorkTimeType(null, 6L, "Акс", false));
        admin = new WorkTimeType(null, 7L, "Административная", false);
//        if (type == null) {
//            return null;
//        } else if (taskType != null && taskType == 3) {
//            return "Административная";
//        } else if (type == 1) {
//            return ;
//        } else if (type == 2) {
//            return "Консультация";
//        } else if (type == 3) {
//            return "Анализ";
//        } else if (type == 4) {
//            return "Тестирование";
//        } else if (type == 5) {
//            return "Анализ ошибки";
//        } else if (type == 6) {
//            return "Акс";
//        } else {
//            return type.toString();
//        }
    }
}
