package ru.darujo.integration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.dto.CustomPageImpl;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Log4j2
@Component
@ConditionalOnMissingClass
public class CalendarServiceIntegration extends ServiceIntegration {
    private WebClient webClientCalendar;

    @Autowired
    public void setWebClientCalendar(WebClient webClientCalendar) {
        this.webClientCalendar = webClientCalendar;
    }

    public List<WeekWorkDto> getWeekTime(Timestamp dateStart, Timestamp dateEnd) {
        return getPeriodTime(dateStart,dateEnd, null);
    }
    public List<WeekWorkDto> getPeriodTime(Timestamp dateStart, Timestamp dateEnd,String period) {
        StringBuilder stringBuilder = getDateTeg(dateStart, dateEnd);
        addTeg(stringBuilder,"period",period);
        try {
            return webClientCalendar.get().uri("/calendar/period/time" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR,"Что-то пошло не так не удалось получить работы за период"))
                    .bodyToFlux(WeekWorkDto.class).collectList()
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public Float getWorkTime(Timestamp dateStart, Timestamp dateEnd) {
        StringBuilder stringBuilder = getDateTeg(dateStart, dateEnd);

        try {
            return webClientCalendar.get().uri("/calendar/work/time" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR,"Что-то пошло не так не удалось получить отпуск за период"))
                    .bodyToMono(Float.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public List<VacationDto> getVacation(String nikName, Timestamp dateStart, Timestamp dateEnd) {
        StringBuilder stringBuilder = getDateTeg(dateStart, dateEnd);
        addTeg(stringBuilder,"nikName",nikName);

        try {
            return Objects.requireNonNull(webClientCalendar.get().uri("/vacation" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR,"Что-то пошло не так не удалось получить отпуск за период"))
                    .bodyToMono(new ParameterizedTypeReference<CustomPageImpl<VacationDto>>() {
                    })
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block()).getContent();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
    public Timestamp getLastWorkDay(String username, Timestamp dateStart, Integer dayMinus, Boolean lastWeek) {

        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"username",username);
        addTeg(stringBuilder,"dateStart",dateStart);
        addTeg(stringBuilder,"dayMinus",dayMinus);
        addTeg(stringBuilder,"lastWeek",lastWeek);

        try {
            return webClientCalendar.get().uri("/vacation/report/user/work/day/last" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR,"Что-то пошло не так не удалось получить отпуск за период httpStatus "))
                    .bodyToMono(Timestamp.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
    public Boolean isWorkDayUser(String username, Timestamp date) throws ResourceNotFoundException {

        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"username",username);
        addTeg(stringBuilder,"date",date);

        try {
            return webClientCalendar.get().uri("/vacation/report/user/work/day" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR,"Что-то пошло не так не удалось получить отпуск за период httpStatus "))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
    public Boolean isDayAfterWeek(Timestamp date, Integer dayMinus) throws ResourceNotFoundException {

        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"date",date);
        addTeg(stringBuilder,"dayMinus",dayMinus);

        try {
            return webClientCalendar.get().uri("/vacation/report/work/day/after/week" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR,"Что-то пошло не так не удалось получить отпуск за период httpStatus "))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    private StringBuilder getDateTeg(Timestamp dateStart, Timestamp dateEnd) {
        if(dateStart == null || dateEnd ==null){
            throw new ResourceNotFoundRunTime("Что-то пошло не так для получения календаря должны быть заданы даты начала и конца"  );
        }
        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"dateStart", dateStart);
        addTeg(stringBuilder,"dateEnd", dateEnd);
        return stringBuilder;
    }

    public Boolean isVacationStart(String nikName,
                                   Integer day) throws ResourceNotFoundException {

        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"nikName",nikName);
        addTeg(stringBuilder,"day",day);

        try {
            return webClientCalendar.get().uri("/vacation/inform/day/begin" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR,"Что-то пошло не так не удалось получить отпуск за период httpStatus "))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
    public Boolean isVacationEnd(String nikName) throws ResourceNotFoundException {

        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"nikName",nikName);

        try {
            return webClientCalendar.get().uri("/vacation/inform/day/end" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR,"Что-то пошло не так не удалось получить отпуск за период httpStatus "))
                    .bodyToMono(Boolean.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public List<VacationDto> userVacationStart(String nikName,
                                           Integer day) throws ResourceNotFoundException {

        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"nikName",nikName);
        addTeg(stringBuilder,"day",day);

        try {
            return webClientCalendar.get().uri("/vacation/inform/user/day/begin" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR,"Что-то пошло не так не удалось получить отпуск за период httpStatus "))
                    .bodyToFlux(VacationDto.class)
                    .collectList()
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

}
