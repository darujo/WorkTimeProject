package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.service.VacationService;

import java.util.List;

@RestController()
@RequestMapping("/v1/vacation/inform")
public class VacationInformController {
    VacationService vacationService;

    @Autowired
    public void setVacationService(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping("/day/begin")
    public Boolean isVacationStart(@RequestParam String nikName,
                                   @RequestParam Integer day) {
        return vacationService.isVacationStart(nikName,day);
    }
    @GetMapping("/day/end")
    public Boolean isVacationEnd(@RequestParam String nikName) {
        return vacationService.isVacationEnd(nikName);
    }
    @GetMapping("/user/day/begin")
    public List<VacationDto> userVacationStart(@RequestParam(required = false) String nikName,
                                               @RequestParam Integer day) {
        return vacationService.userVacationStart(nikName,day);
    }
}
