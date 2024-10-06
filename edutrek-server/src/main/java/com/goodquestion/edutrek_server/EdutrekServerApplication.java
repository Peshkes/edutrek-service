package com.goodquestion.edutrek_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EdutrekServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EdutrekServerApplication.class, args);
	}
}
