package ru.darujo.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.darujo.dto.ListString;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.WorkDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Component
public class CalendarServiceIntegration {
    private WebClient webClientCalendar;

    @Autowired
    public void setWebClientCalendar(WebClient webClientCalendar) {
        this.webClientCalendar = webClientCalendar;
    }


    public List<WeekWorkDto> getWeekTime(Timestamp dateStart, Timestamp dateEnd) {
        return webClientCalendar.get().uri("/weektime?dateStart=" + dateStart + "&dateEnd=" + dateEnd)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResourceNotFoundException("Что-то пошло не так не удалось получить работы за период")))
                .bodyToFlux(WeekWorkDto.class).collectList()
                .block();

    }

}
