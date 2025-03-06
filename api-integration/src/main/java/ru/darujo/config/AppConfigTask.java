package ru.darujo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.darujo.properties.TaskServiceProperty;

@Configuration
@PropertySource("classpath:integration.properties")
@EnableConfigurationProperties(
        TaskServiceProperty.class
)
public class AppConfigTask extends WebClientConfig {
    private TaskServiceProperty taskServiceProperty;
    @Autowired
    public void setTaskServiceProperty(TaskServiceProperty taskServiceProperty){
        this.taskServiceProperty = taskServiceProperty;
    }

    @Bean
    public WebClient webClientTask(){
        return webClient(taskServiceProperty);
    }
}
