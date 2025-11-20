package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.EnumHelper;
import ru.darujo.convertor.WorkAgreementRequestConvertor;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.ratestage.StatusRequest;
import ru.darujo.dto.ratestage.WorkAgreementRequestDto;
import ru.darujo.dto.ratestage.WorkAgreementRequestEditDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.service.WorkAgreementRequestService;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/v1/agreement/request")
public class WorkAgreementRequestController {
    private WorkAgreementRequestService workAgreementRequestService;
    @Autowired
    public void setWorkCriteria(WorkAgreementRequestService workAgreementRequestService) {
        this.workAgreementRequestService = workAgreementRequestService;
    }


    @GetMapping("/{id}")
    public WorkAgreementRequestEditDto WorkAgreementRequestEdit(@PathVariable long id) {
        return WorkAgreementRequestConvertor.getWorkAgreementRequestEditDto(workAgreementRequestService.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдена разбивка")));
    }

    @PostMapping("")
    public WorkAgreementRequestEditDto WorkAgreementRequestSave(@RequestHeader String username, @RequestBody WorkAgreementRequestEditDto workAgreementRequestEditDto) {
        if(workAgreementRequestEditDto.getNikName() == null || workAgreementRequestEditDto.getNikName().isEmpty()){
            workAgreementRequestEditDto.setNikName(username);
        }
        if (workAgreementRequestEditDto.getTimestamp() == null){
            workAgreementRequestEditDto.setTimestamp(new Timestamp(System.currentTimeMillis()));
        }
        return WorkAgreementRequestConvertor.getWorkAgreementRequestEditDto(workAgreementRequestService.saveWorkCriteria(WorkAgreementRequestConvertor.getWorkAgreementRequest(workAgreementRequestEditDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkAgreementRequest(@PathVariable long id) {
        workAgreementRequestService.deleteWorkRequest(id);
    }

    @GetMapping("")
    public List<WorkAgreementRequestEditDto> WorkAgreementRequestEditList(@RequestParam Long workId) {
        return workAgreementRequestService.findWorkAgreementRequest(workId).stream().map(WorkAgreementRequestConvertor::getWorkAgreementRequestEditDto).collect(Collectors.toList());
    }
    @GetMapping("/full")
    public List<WorkAgreementRequestDto> WorkAgreementRequestList(@RequestParam Long workId) {
        return workAgreementRequestService.findWorkAgreementRequest(workId).stream().map(WorkAgreementRequestConvertor::getWorkAgreementRequestDto).collect(Collectors.toList());
    }
    @GetMapping("/status")
    public List<AttrDto<Enum<?>>> StatusList() {
        return EnumHelper.getList(StatusRequest.values());
    }
}