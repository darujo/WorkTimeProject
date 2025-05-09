package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.converter.VacationConvertor;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.dto.parsing.DateParser;
import ru.darujo.model.Vacation;
import ru.darujo.service.VacationService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@RestController()
@RequestMapping("/v1/vacation")
public class VacationController extends DateParser {
    private VacationService vacationService;

    @Autowired
    public void setVacationService(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping("/{id}")
    public VacationDto VacationEdit(@PathVariable long id) {
        return getVacationDtoAndAddDay(vacationService.findById(id));
    }

    @PostMapping("")
    public VacationDto VacationSave(@RequestHeader String username,
                                    @RequestBody VacationDto vacationDto) {
        if (vacationDto.getNikName() == null) {
            vacationDto.setNikName(username);
        }
        return getVacationDtoAndAddDay(vacationService.saveVacation(getVacationUpdateAndConvert(vacationDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteVacation(@PathVariable long id) {
        vacationService.deleteVacation(id);
    }

    @GetMapping("")
    public Page<VacationDto> VacationPage(@RequestParam(required = false) String nikName,
                                          @RequestParam(required = false, name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                          @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        Timestamp dateStart = stringToDate(dateStartStr, "dateStart = ");
        Timestamp dateEnd = stringToDate(dateEndStr, "dateEnd = ");

        return vacationService.findAll(nikName,dateStart,dateEnd,page,size).map(this::getVacationDtoAndAddFio);
    }

    private VacationDto getVacationDtoAndAddDay(Vacation vacation) {
        VacationDto vacationDto = VacationConvertor.getVacationDto(vacation);
        vacationDto.setDays(vacationService.getDayNotHoliday(vacationDto.getDateStart(), vacationDto.getDateEnd()));
        return vacationDto;
    }

    private VacationDto getVacationDtoAndAddFio(Vacation vacation) {
        VacationDto vacationDto = getVacationDtoAndAddDay(vacation);
        vacationService.updFio(vacationDto);
        return vacationDto;
    }

    private Vacation getVacationUpdateAndConvert(VacationDto vacationDto) {
        vacationDto.setDateEnd(vacationService.getNewDate(vacationDto.getDateStart(), vacationDto.getDateEnd(), vacationDto.getDays()));
        return VacationConvertor.getVacation(vacationDto);
    }

}
