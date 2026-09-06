package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Code;

import java.util.List;

@Slf4j
@Service
public class TaskStatus {
    private static CodeTypeService codeTypeService;
    private static Code parent;

    @PostConstruct

    private void initCode() {
        parent = addStatus(null, "taskStatus", "Справочник статусов задач", 100);
        addStatus("analysis", "Анализ", 10);
        addStatus("develop", "Разработка", 30);
        addStatus("testing", "Тестирование", 50);
        addStatus("end", "Завершена", 100);

    }

    public void addStatus(String code, String name, Integer sort) {
        addStatus(parent, code, name, sort);
    }

    public Code addStatus(Code parent, String code, String name, Integer sort) {
        Code codeSave = null;
        try {
            codeSave = codeTypeService.findOne(parent, code);
        } catch (ResourceNotFoundRunTime ignore) {
        }
        Code codeNew = new Code(null, parent, code, name, sort, null);
        if (codeSave == null) {
            codeSave = codeNew;
            return codeTypeService.save(codeSave);
        } else if (!codeSave.equals(codeNew)) {
            codeSave.setName(name);
            codeSave.setSort(sort);
            return codeTypeService.save(codeSave);
        }
        return codeSave;
    }

    public static List<Code> getList() {
        return codeTypeService.getCodes(parent, null).getContent();
    }

    public static String getName(@NonNull String code) {
        return codeTypeService.findOne(parent, code).getName();
    }


    @Autowired
    public void setCodeTypeService(CodeTypeService codeTypeService) {
        TaskStatus.codeTypeService = codeTypeService;
        try {
            parent = codeTypeService.findOne(null, "taskStatus");
        } catch (ResourceNotFoundRunTime ignore) {

        }

    }
}
