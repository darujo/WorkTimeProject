package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.converter.VacationConvertor;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.model.Vacation;
import ru.darujo.service.VacationService;

import java.util.List;
import java.util.stream.Collectors;

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
        return VacationConvertor.getVacationDto(vacationService.findById(id));
    }

    @PostMapping("")
    public VacationDto VacationSave(@RequestHeader String username,
                                    @RequestBody VacationDto vacationDto) {
        if(vacationDto.getNikName() == null){
            vacationDto.setNikName(username);
        }
        return VacationConvertor.getVacationDto(vacationService.saveVacation(VacationConvertor.getVacation(vacationDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteVacation(@PathVariable long id) {
        vacationService.deleteVacation(id);
    }

    @GetMapping("")
    public List<VacationDto> VacationPage(@RequestParam (required = false) String nikName) {
        return vacationService.findAll(nikName).stream().map(this::getVacationDtoAndAddFio).collect(Collectors.toList());
    }

    private VacationDto getVacationDtoAndAddFio(Vacation vacation){
        VacationDto vacationDto = VacationConvertor.getVacationDto(vacation);
        vacationService.updFio(vacationDto);
        return vacationDto;
    }



}
