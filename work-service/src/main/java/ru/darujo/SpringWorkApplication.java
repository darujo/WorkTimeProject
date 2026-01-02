package ru.darujo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringWorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWorkApplication.class, args);
	}

}
