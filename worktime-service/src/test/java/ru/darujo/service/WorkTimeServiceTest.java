package ru.darujo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.darujo.dto.WorkTimeDto;
import ru.darujo.model.WorkTime;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class WorkTimeServiceTest {
    @Autowired
    WorkTimeService workTimeService;
    @Autowired
    WebTestClient webTestClient;
    @Test
    void findById() {
        WorkTime workTime = new WorkTime();
        workTime.setId(1L);
        workTime = workTimeService.saveWorkTime(workTime);


        WorkTimeDto workTimeByHttp = webTestClient.get()
                .uri("/v1/worktime/" + workTime.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(WorkTimeDto.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(workTime.getId(), workTimeByHttp.getId());
    }

    @Test
    void saveWorkTime() {
    }

    @Test
    void deleteWorkTime() {
    }
    @Test
    void testFindByIdNotFound() {
        WorkTime workTime = new WorkTime();
        workTime.setId(1L);
        workTime.setWorkDate(new Date());
        workTime = workTimeService.saveWorkTime(workTime);
        Long workTimeId = workTime.getId();
        workTimeService.deleteWorkTime(workTimeId);

        webTestClient.get()
                .uri("v1/worktime/" + workTimeId)
                .exchange()
                .expectStatus().isNotFound();
    }
}