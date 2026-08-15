package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.converter.VacationConvertor;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.user.UserFio;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegrationImp;
import ru.darujo.model.Vacation;
import ru.darujo.repository.VacationRepository;
import ru.darujo.specifications.Specifications;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
public class VacationService {
    VacationRepository vacationRepository;

    @Autowired
    public void setVacationRepository(VacationRepository vacationRepository) {
        this.vacationRepository = vacationRepository;
    }

    UserServiceIntegrationImp userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegrationImp userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    CalendarService calendarService;

    @Autowired
    public void setCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    public Vacation findById(long id) {
        return vacationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдена запись с ID" + id));
    }

    private String dateToText(LocalDate date) {
        return DateHelper.dateToDDMMYYYY(date);
    }

    public void checkVacation(Vacation vacation) {
        if (vacation.getNikName() == null) {
            throw new ResourceNotFoundRunTime("ФИО должно быть заполнено");
        }
        if (vacation.getDateStart() == null) {
            throw new ResourceNotFoundRunTime("Дата начала периода должны быть заполнены");
        }
        if (vacation.getDateEnd() == null) {
            throw new ResourceNotFoundRunTime("Дата конца периода должны быть заполнены");
        }
        if (calendarService.isHoliday(vacation.getDateEnd())) {
            throw new ResourceNotFoundRunTime("Дата конца отпуска не может быть праздником");
        }
        Vacation vacationSave = findOneDateBetween(vacation.getNikName(), null, "dateStart", vacation.getDateStart(), vacation.getDateEnd());
        if (vacationSave != null && !vacationSave.getId().equals(vacation.getId())) {
            throw new ResourceNotFoundRunTime("Отсутствие пересекаются с Отсутствие " + dateToText(vacationSave.getDateStart()) + " - " + dateToText(vacationSave.getDateEnd()));
        }
        vacationSave = findOneDateBetween(vacation.getNikName(), null, "dateEnd", vacation.getDateStart(), vacation.getDateEnd());
        if (vacationSave != null && !vacationSave.getId().equals(vacation.getId())) {
            throw new ResourceNotFoundRunTime("Отпуск пересекаются с отпуском " + dateToText(vacationSave.getDateStart()) + " - " + dateToText(vacationSave.getDateEnd()));
        }
        vacationSave = findOneDateInVacation(vacation.getNikName(), vacation.getDateStart());
        if (vacationSave != null && !vacationSave.getId().equals(vacation.getId())) {
            throw new ResourceNotFoundRunTime("Отпуск пересекаются с отпуском " + dateToText(vacationSave.getDateStart()) + " - " + dateToText(vacationSave.getDateEnd()));
        }
        // вторую дату проверять не надо так как этот случай покрывается предыдущими случаями
    }

    public void setEndVacation(String nikName, LocalDate date) {
        Vacation vacation = findOneDateInVacation(nikName, date);
        if (vacation != null && vacation.getDynamic() && date.isBefore(vacation.getDateEnd())) {
            vacation.setDateEnd(date);
            checkVacation(vacation);
            vacationRepository.save(vacation);
        }

    }
    private LocalDate addDay(LocalDate date, int day) {
        return date.plusDays(day);
    }

    @Transactional
    public Vacation saveVacation(Vacation vacation) {
        checkVacation(vacation);
        LocalDate date = addDay(vacation.getDateStart(), -1);
        Vacation vacationSave = findOneDateBetween(vacation.getNikName(), vacation.getType(), "dateEnd", date, date);
        if (vacationSave != null) {
            vacation.setDateStart(vacationSave.getDateStart());
            vacationRepository.delete(vacationSave);
        }
        date = addDay(vacation.getDateEnd(), 1);
        vacationSave = findOneDateBetween(vacation.getNikName(), vacation.getType(), "dateStart", date, date);
        if (vacationSave != null) {
            vacation.setDateEnd(vacationSave.getDateEnd());
            vacationRepository.delete(vacationSave);
        }
        if (!calendarService.existWorkDay(vacation.getDateStart(), vacation.getDateEnd())) {
            throw new ResourceNotFoundRunTime("Отпуск должен содержать рабочий день");
        }
        return vacationRepository.save(vacation);
    }

    public void deleteVacation(long id) {
        vacationRepository.deleteById(id);
    }

    public Page<@NonNull Vacation> findAll(String nikName, String type, LocalDate dateStart, LocalDate dateEnd, Integer page, Integer size) {
        List<String> users = Objects.requireNonNull(userServiceIntegration.getUserDTOs(nikName)).stream().map(UserDto::getNikName).collect(Collectors.toList());
        Specification<@NonNull Vacation> specification = getVacationSpecification(null, type, dateStart, dateEnd, users);
        return Specifications.findAll(vacationRepository, page == null ? null : page - 1, size, specification, List.of("nikName", "dateStart"));
    }

    private static Specification<@NonNull Vacation> getVacationSpecification(String nikName, String type, LocalDate dateStart, LocalDate dateEnd, List<String> userList) {
        Specification<@NonNull Vacation> specification;
        specification = Specification.unrestricted();
        specification = Specifications.eq(specification, "nikName", nikName);
        specification = Specifications.in(specification, "nikName", userList);
        specification = Specifications.eq(specification, "type", type);
        specification = Specifications.ge(specification, "dateEnd", dateStart);
        specification = Specifications.le(specification, "dateStart", dateEnd);
        return specification;
    }

    public Vacation findOneDateBetween(String nikName, String type, String field, LocalDate dateGe, LocalDate dateLe) {
        Specification<@NonNull Vacation> specification = getVacationSpecification(
                nikName,
                type,
                null,
                null,
                null);

        if (dateGe.equals(dateLe)) {
            specification = Specifications.eq(specification, field, dateGe);
        } else {
            specification = Specifications.ge(specification, field, dateGe);
            specification = Specifications.le(specification, field, dateLe);
        }
        return vacationRepository.findOne(specification).orElse(null);
    }

    public Vacation findOneDateInVacation(String nikName, LocalDate date) {
        Specification<@NonNull Vacation> specification;
        specification = getVacationSpecification(nikName, null, date, date, null);
        return vacationRepository.findOne(specification).orElse(null);
    }

    public void updFio(UserFio userFio) {
        userServiceIntegration.updFio(userFio);
    }

    public int getDayNotHoliday(LocalDate dateStart, LocalDate dateEnd) {
        return calendarService.getDayNotHoliday(dateStart, dateEnd);
    }

    public LocalDate getNewDate(LocalDate dateStart, LocalDate dateEnd, Integer days) {
        if (dateStart == null || days == null || days < 1) {
            return dateEnd;
        }
        return calendarService.getDateEndNotHoliday(dateStart, days);
    }

    public List<VacationDto> userVacationStart(String nikName, int day, List<String> typeList) {
        LocalDate date = addDay(LocalDate.now(), day);
        return userVacationStart(nikName, date, typeList);
    }

    public List<VacationDto> userVacationStart(String nikName, LocalDate dateStart, List<String> typeList) {
        Specification<@NonNull Vacation> specification;
        specification = Specifications.eq(null, "nikName", nikName);
        specification = Specifications.eq(specification, "dateStart", dateStart);
        specification = Specifications.in(specification, "type", typeList);
        return vacationRepository.findAll(specification).stream().map(this::getVacationDtoAndAddFio).toList();
    }

    public boolean isVacationStart(String nikName, int day) {
        LocalDate date = addDay(LocalDate.now(), day);
        return findVacationStart(nikName, date) != null;
    }

    public Vacation findVacationStart(String nikName, LocalDate date) {
        return findOneDateBetween(nikName, null, "dateStart", date, date);
    }

    public Boolean isVacationEnd(String nikName) {
        return findVacationEnd(nikName, LocalDate.now()) != null;
    }

    public Vacation findVacationEnd(String nikName, LocalDate date) {
        return findOneDateBetween(nikName, null, "dateEnd", date, date);
    }

    public VacationDto getVacationDtoAndAddDay(Vacation vacation) {
        VacationDto vacationDto = VacationConvertor.getVacationDto(vacation);
        vacationDto.setDays(getDayNotHoliday(DateHelper.zDTToLD(vacationDto.getDateStart()), DateHelper.zDTToLD(vacationDto.getDateEnd())));
        return vacationDto;
    }

    public VacationDto getVacationDtoAndAddFio(Vacation vacation) {
        VacationDto vacationDto = getVacationDtoAndAddDay(vacation);
        updFio(vacationDto);
        return vacationDto;
    }

    public Vacation getVacationUpdateAndConvert(VacationDto vacationDto) {
        vacationDto.setDateEnd(DateHelper.getZDT(getNewDate(DateHelper.zDTToLD(vacationDto.getDateStart()), DateHelper.zDTToLD(vacationDto.getDateEnd()), vacationDto.getDays())));
        return VacationConvertor.getVacation(vacationDto);
    }


    public Vacation nextVacation(String nikName, LocalDate day) {
        Specification<@NonNull Vacation> specification;
        specification = Specifications.eq(null, "nikName", nikName);
        specification = Specifications.ge(specification, "dateStart", day);

        Page<Vacation> page = vacationRepository.findAll(specification, PageRequest.of(0, 1, Objects.requireNonNull(Specifications.parseSort(List.of("nikName", "dateStart")))));
        if (page.getTotalElements() > 0) {
            return page.getContent().get(0);
        }
        return null;
    }
}
