package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.service.RateService;


@RestController()
@RequestMapping("/v1/rate")
public class RateController {
    private RateService rateService;
    @Autowired
    public void setRateService(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/compare/sc")
    public AttrDto<Float> ComparisonStageCriteria(@RequestParam Long workId) {
        return rateService.ComparisonStageCriteria(workId);
    }

    @GetMapping("/compare/st")
    public AttrDto<Float> ComparisonStageType(@RequestParam Long workId) {
        return rateService.ComparisonStageType(workId);
    }
    @GetMapping("/compare/ct")
    public AttrDto<Float> ComparisonCriteriaType(@RequestParam Long workId) {
        return rateService.ComparisonCriteriaType(workId);
    }
    @GetMapping("/time/all")
    public WorkStageDto AllTime(@RequestParam Long workId,
                                @RequestParam (defaultValue = "false") boolean loadFact,
                                @RequestParam (defaultValue = "false") Long projectId
    ) {
        return rateService.AllTime(workId, projectId, loadFact);

    }

}