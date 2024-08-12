package com.example.shop4All_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Shop4AllBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Shop4AllBackendApplication.class, args);
	}

}
