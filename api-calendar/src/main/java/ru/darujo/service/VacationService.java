package ru.darujo.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.user.UserFio;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.Vacation;
import ru.darujo.repository.VacationRepository;
import ru.darujo.repository.specification.VacationSpecifications;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@Primary
public class VacationService {
    VacationRepository vacationRepository;

    @Autowired
    public void setVacationRepository(VacationRepository vacationRepository) {
        this.vacationRepository = vacationRepository;
    }

    UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
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

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private String dateToText(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }

    public void checkVacation(Vacation vacation) {
        if (vacation.getNikName() == null) {
            throw new ResourceNotFoundRunTime("ФИО должно быть заполнено");
        }
        if (vacation.getDateStart() == null || vacation.getDateEnd() == null) {
            throw new ResourceNotFoundRunTime("Дата начала и конца периода должны быть заполнены");
        }
        if (calendarService.isHoliday(vacation.getDateEnd())) {
            throw new ResourceNotFoundRunTime("Дата конца отпуска не может быть праздником");
        }
        Vacation vacationSave = findOneDateBetween(vacation.getNikName(), "dateStart", vacation.getDateStart(), vacation.getDateEnd());
        if (vacationSave != null && !vacationSave.getId().equals(vacation.getId())) {
            throw new ResourceNotFoundRunTime("Отпуск пересекаются с отпуском " + dateToText(vacationSave.getDateStart()) + " - " + dateToText(vacationSave.getDateEnd()));
        }
        vacationSave = findOneDateBetween(vacation.getNikName(), "dateEnd", vacation.getDateStart(), vacation.getDateEnd());
        if (vacationSave != null && !vacationSave.getId().equals(vacation.getId())) {
            throw new ResourceNotFoundRunTime("Отпуск пересекаются с отпуском " + dateToText(vacationSave.getDateStart()) + " - " + dateToText(vacationSave.getDateEnd()));
        }
        vacationSave = findOneDateInVacation(vacation.getNikName(), vacation.getDateStart());
        if (vacationSave != null && !vacationSave.getId().equals(vacation.getId())) {
            throw new ResourceNotFoundRunTime("Отпуск пересекаются с отпуском " + dateToText(vacationSave.getDateStart()) + " - " + dateToText(vacationSave.getDateEnd()));
        }
        // вторую дату проверять не надо  так как этот  случай покрывается предыдущими случаями
    }

    private Timestamp addDay(Timestamp date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK, day);
        return new Timestamp(cal.getTime().getTime());
    }

    @Transactional
    public Vacation saveVacation(Vacation vacation) {
        checkVacation(vacation);
        Timestamp date = addDay(vacation.getDateStart(), -1);
        Vacation vacationSave = findOneDateBetween(vacation.getNikName(), "dateEnd", date, date);
        if (vacationSave != null) {
            vacation.setDateStart(vacationSave.getDateStart());
            vacationRepository.delete(vacationSave);
        }
        date = addDay(vacation.getDateEnd(), 1);
        vacationSave = findOneDateBetween(vacation.getNikName(), "dateStart", date, date);
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

    public Page<Vacation> findAll(String nikName, Timestamp dateStart, Timestamp dateEnd, Integer page, Integer size) {
        List<String> users = Objects.requireNonNull(userServiceIntegration.getUserDTOs(nikName)).stream().map(UserDto::getNikName).collect(Collectors.toList());
        Specification<Vacation> specification = Specification.where(null);
        specification = VacationSpecifications.in(specification, "nikName", users);
        specification = VacationSpecifications.dateGe(specification, "dateEnd", dateStart);
        specification = VacationSpecifications.dateLe(specification, "dateStart", dateEnd);

        if (page != null && size != null) {
            return vacationRepository.findAll(specification
                    , PageRequest.of(page - 1, size));
        } else {
            return new PageImpl<>(vacationRepository.findAll(specification));
        }
    }

    public Vacation findOneDateBetween(String nikName, String field, Date dateGe, Date dateLe) {
        Specification<Vacation> specification = Specification.where(null);
        specification = VacationSpecifications.eq(specification, "nikName", nikName);
        specification = VacationSpecifications.dateGe(specification, field, dateGe);
        specification = VacationSpecifications.dateLe(specification, field, dateLe);
        return vacationRepository.findOne(specification).orElse(null);
    }

    public Vacation findOneDateInVacation(String nikName, LocalDate localDate) {
        return findOneDateInVacation(nikName, Timestamp.valueOf(localDate.atStartOfDay()));

    }

    public Vacation findOneDateInVacation(String nikName, Date date) {
        Specification<Vacation> specification = Specification.where(null);
        specification = VacationSpecifications.eq(specification, "nikName", nikName);
        specification = VacationSpecifications.dateLe(specification, "dateStart", date);
        specification = VacationSpecifications.dateGe(specification, "dateEnd", date);
        return vacationRepository.findOne(specification).orElse(null);
    }

    private final Map<String, UserDto> userDtoMap = new HashMap<>();

    public void updFio(UserFio userFio) {
        try {
            if (userFio.getNikName() != null) {
                UserDto userDto = userDtoMap.get(userFio.getNikName());
                if (userDto == null) {
                    userDto = userServiceIntegration.getUserDto(null, userFio.getNikName());
                    userDtoMap.put(userFio.getNikName(), userDto);
                }
                userFio.setFirstName(userDto.getFirstName());
                userFio.setLastName(userDto.getLastName());
                userFio.setPatronymic(userDto.getPatronymic());
            }
        } catch (ResourceNotFoundRunTime e) {
            log.error(e.getMessage());
            userFio.setFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }

    public int getDayNotHoliday(Date dateStart, Date dateEnd) {
        return calendarService.getDayNotHoliday(dateStart, dateEnd);
    }

    public Timestamp getNewDate(Timestamp dateStart, Timestamp dateEnd, Integer days) {
        if (dateStart == null || days == null || days < 1) {
            return dateEnd;
        }
        return calendarService.getDateEndNotHoliday(dateStart, days);
    }

}
