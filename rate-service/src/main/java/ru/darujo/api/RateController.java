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
    public AttrDto ComparisonStageCriteria(@RequestParam Long workId,
                                           @RequestParam (defaultValue = "false") boolean loadFact ) {
        return rateService.ComparisonStageCriteria(workId,loadFact);
    }

    @GetMapping("/time/all")
    public WorkStageDto AllTime(@RequestParam Long workId,
                                @RequestParam (defaultValue = "false") boolean loadFact ) {
        return rateService.AllTime(workId,loadFact);

    }

}