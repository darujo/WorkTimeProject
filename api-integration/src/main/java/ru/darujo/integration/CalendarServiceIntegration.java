package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.CustomPageImpl;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;


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
                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы за период")))
                    .bodyToFlux(WeekWorkDto.class).collectList()
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
                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить отпуск за период")))
                    .bodyToMono(Float.class)
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
                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить отпуск за период")))
                    .bodyToMono(new ParameterizedTypeReference<CustomPageImpl<VacationDto>>() {
                    })
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
                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить отпуск за период httpStatus " + clientResponse.statusCode())))
                    .bodyToMono(Timestamp.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }
    public Boolean isDayAfterWeek(Timestamp date, Integer dayMinus) {

        StringBuilder stringBuilder = new StringBuilder();
        addTeg(stringBuilder,"date",date);
        addTeg(stringBuilder,"dayMinus",dayMinus);

        try {
            return webClientCalendar.get().uri("/vacation/report/work/day/after/week" + stringBuilder)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить отпуск за период httpStatus " + clientResponse.statusCode())))
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
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
}
