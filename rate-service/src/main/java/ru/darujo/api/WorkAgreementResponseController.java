package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.EnumHelper;
import ru.darujo.convertor.WorkAgreementResponseConvertor;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.ratestage.StatusResponse;
import ru.darujo.dto.ratestage.WorkAgreementResponseDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.service.WorkAgreementResponseService;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/v1/agreement/response")
public class WorkAgreementResponseController {
    private WorkAgreementResponseService workAgreementResponseService;
    @Autowired
    public void setWorkCriteria(WorkAgreementResponseService workAgreementResponseService) {
        this.workAgreementResponseService = workAgreementResponseService;
    }


    @GetMapping("/{id}")
    public WorkAgreementResponseDto WorkAgreementResponseEdit(@PathVariable long id) {
        return WorkAgreementResponseConvertor.getWorkAgreementResponseDTO(workAgreementResponseService.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдена разбивка")));
    }

    @PostMapping("")
    public WorkAgreementResponseDto WorkAgreementResponseSave(@RequestHeader String username,
                                                              @RequestBody WorkAgreementResponseDto workAgreementResponseDto) {
        if(workAgreementResponseDto.getNikName() == null || workAgreementResponseDto.getNikName().isEmpty()){
            workAgreementResponseDto.setNikName(username);
        }
        if (workAgreementResponseDto.getTimestamp() == null){
            workAgreementResponseDto.setTimestamp(new Timestamp(System.currentTimeMillis()));
        }
        return WorkAgreementResponseConvertor.getWorkAgreementResponseDTO(workAgreementResponseService.saveWorkAgreementResponse(WorkAgreementResponseConvertor.getWorkAgreementResponse(workAgreementResponseDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkAgreementResponse(@PathVariable long id) {
        workAgreementResponseService.deleteWorkAgreementResponse(id);
    }

    @GetMapping("")
    public List<WorkAgreementResponseDto> WorkAgreementResponseList(@RequestParam Long workId,
                                                           @RequestParam Long requestId) {
        return workAgreementResponseService.findWorkAgreementResponse(workId,requestId).stream().map(WorkAgreementResponseConvertor::getWorkAgreementResponseDTO).collect(Collectors.toList());
    }
    @GetMapping("/status")
    public List<AttrDto<Enum<?>>> STatusList() {
        return EnumHelper.getList(StatusResponse.values());
    }

}