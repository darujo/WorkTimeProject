package ru.darujo.api;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.assistant.helper.EnumHelper;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.service.VacationService;
import ru.darujo.type.VacationType;

import java.time.ZonedDateTime;
import java.util.List;


@RestController()
@RequestMapping("/v1/vacation")
public class VacationController {
    private VacationService vacationService;

    @Autowired
    public void setVacationService(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping("/{id}")
    public VacationDto VacationEdit(@PathVariable long id) {
        return vacationService.getVacationDtoAndAddDay(vacationService.findById(id));
    }

    @PostMapping("")
    public VacationDto VacationSave(@RequestHeader String username,
                                    @RequestBody VacationDto vacationDto) {
        if (vacationDto.getNikName() == null) {
            vacationDto.setNikName(username);
        }
        return vacationService.getVacationDtoAndAddDay(vacationService.saveVacation(vacationService.getVacationUpdateAndConvert(vacationDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteVacation(@PathVariable long id) {
        vacationService.deleteVacation(id);
    }

    @GetMapping("")
    public Page<@NonNull VacationDto> VacationPage(@RequestHeader(required = false)
                                                   String username,
                                                   @RequestParam(required = false)
                                                   String nikName,
                                                   @RequestParam(required = false)
                                                       String type,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                       ZonedDateTime dateStart,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                       ZonedDateTime dateEnd,
                                                   @RequestParam(required = false)
                                                   Integer page,
                                                   @RequestParam(defaultValue = "10")
                                                   Integer size) {
        if (nikName != null && nikName.equals("current")) {
            nikName = username;
        }
        return vacationService.findAll(nikName, type, DateHelper.zDTToLD(dateStart), DateHelper.zDTToLD(dateEnd), page, size).map(vacationService::getVacationDtoAndAddFio);
    }

    @GetMapping("/types")
    public List<AttrDto<Enum<?>>> StatusList() {
        return EnumHelper.getList(VacationType.values());
    }

    @GetMapping("/set/end")
    public void setVacationEnd(@RequestParam String nikName,
                               @RequestParam ZonedDateTime date) {
        vacationService.setEndVacation(nikName, DateHelper.zDTToLD(date));
    }

}
