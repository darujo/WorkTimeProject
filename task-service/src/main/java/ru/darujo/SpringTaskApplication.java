package ru.darujo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTaskApplication.class, args);
	}

}
