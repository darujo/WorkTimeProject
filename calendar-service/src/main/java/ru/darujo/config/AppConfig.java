package ru.darujo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.darujo.repository.DayInfoRepository;
import ru.darujo.service.DayInfoService;
import ru.darujo.utils.calendar.ProductionCalendar;
import ru.darujo.utils.calendar.days.RU_Days;

@Configuration
@Slf4j
public class AppConfig {
    @Bean("dayInfoService")
    @ConditionalOnBooleanProperty(prefix = "service.calendar", name = "db")
    public DayInfoService getDayInfoService(DayInfoRepository dayInfoRepository) {
        return new DayInfoService(dayInfoRepository);
    }

    @Bean("productionCalendar")
    @ConditionalOnBean(DayInfoService.class)
    public ProductionCalendar getCalendar(DayInfoService dayInfoService) {
        return new ProductionCalendar(new RU_Days(dayInfoService));
    }

    @Bean("productionCalendar")
    @ConditionalOnMissingBean(name = "productionCalendar")
    public ProductionCalendar getDefaultCalendar() {
        log.warn("Хранение календаря в памяти");
        return new ProductionCalendar();
    }
}
