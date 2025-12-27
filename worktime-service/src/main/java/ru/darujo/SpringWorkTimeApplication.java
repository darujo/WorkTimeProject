package ru.darujo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringWorkTimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWorkTimeApplication.class, args);
	}

}
