package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
@ConditionalOnMissingClass
public class CalendarServiceIntegration {
    private WebClient webClientCalendar;

    @Autowired
    public void setWebClientCalendar(WebClient webClientCalendar) {
        this.webClientCalendar = webClientCalendar;
    }


    public List<WeekWorkDto> getWeekTime(Timestamp dateStart, Timestamp dateEnd) {
        try {
            return webClientCalendar.get().uri("/weektime?dateStart=" + dateToText(dateStart) + "&dateEnd=" + dateToText(dateEnd))
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить работы за период")))
                    .bodyToFlux(WeekWorkDto.class).collectList()
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public Float getWorkTime(Timestamp dateStart, Timestamp dateEnd) {
        try {
            return webClientCalendar.get().uri("/worktime?dateStart=" + dateToText(dateStart) + "&dateEnd=" + dateToText(dateEnd))
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить работы за период")))
                    .bodyToMono(Float.class)
                    .block();
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Что-то пошло не так не удалось получить Календатрь (api-calendar) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private String dateToText(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date) + "T00:00:00.000Z";
    }
}
