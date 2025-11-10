package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.service.VacationService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

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
    public Page<VacationDto> VacationPage(@RequestHeader String username,
                                          @RequestParam(required = false) String nikName,
                                          @RequestParam(required = false, name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                          @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        Timestamp dateStart = DataHelper.DTZToDate(dateStartStr, "dateStart = ");
        Timestamp dateEnd = DataHelper.DTZToDate(dateEndStr, "dateEnd = ");
        if (nikName != null && nikName.equals("current")){
            nikName = username;
        }
        return vacationService.findAll(nikName,dateStart,dateEnd,page,size).map(vacationService::getVacationDtoAndAddFio);
    }


}
