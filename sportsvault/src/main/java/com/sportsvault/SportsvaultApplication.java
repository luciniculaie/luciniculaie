package com.sportsvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SportsvaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsvaultApplication.class, args);
	}



}
