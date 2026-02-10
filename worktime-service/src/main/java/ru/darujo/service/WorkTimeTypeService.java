package ru.darujo.service;

import ru.darujo.model.WorkTimeTypeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkTimeTypeService {
    private static final Map<Integer, WorkTimeTypeDto> workTimeTypeMap = new HashMap<>();
    private static WorkTimeTypeDto admin;

    public static void init() {
        if (workTimeTypeMap.size() > 0) {
            return;
        }
        List<Long> analisList = new ArrayList<>();
        analisList.add(2L);
        workTimeTypeMap.put(1, new WorkTimeTypeDto(null, 1, "Разработка", true));
        workTimeTypeMap.put(2, new WorkTimeTypeDto(null, 2, "Консультация", false));
        workTimeTypeMap.put(3, new WorkTimeTypeDto(null, 3, "Анализ", false));
        workTimeTypeMap.put(4, new WorkTimeTypeDto(null, 4, "Тестирование", false));
        workTimeTypeMap.put(5, new WorkTimeTypeDto(null, 5, "Анализ ошибки", false));
        workTimeTypeMap.put(6, new WorkTimeTypeDto(null, 6, "Акс", false));

        admin = new WorkTimeTypeDto(null, 7, "Административная", false);
        workTimeTypeMap.put(8, new WorkTimeTypeDto(analisList, 8, "Согласование Тз", false));
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

    public static String getName(Integer type) {
        init();
        WorkTimeTypeDto workTimeTypeDto = workTimeTypeMap.get(type);
        if (workTimeTypeDto != null) {
            return workTimeTypeDto.getName();
        }
        return type.toString();
    }

    public static String getAdminName() {
        init();
        return admin.getName();
    }

    public static List<WorkTimeTypeDto> getTypeDtoList(Long projectId, Boolean develop) {
        init();
        List<WorkTimeTypeDto> typesList;
        typesList = workTimeTypeMap.values().stream().filter(workTimeTypeDto ->
                        (projectId == null
                                || workTimeTypeDto.getProjectList() == null
                                || workTimeTypeDto.getProjectList().contains(projectId))
                                && (develop == null
                                || workTimeTypeDto.isDevelop() == develop)).toList();
        return typesList;
    }
    public static List<Integer> getTypes(Boolean develop) {
        return getTypeDtoList(null, develop).stream()
                .map(WorkTimeTypeDto::getCode).toList();
    }

}
