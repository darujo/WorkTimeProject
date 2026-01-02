package ru.darujo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringRateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRateApplication.class, args);
	}

}
