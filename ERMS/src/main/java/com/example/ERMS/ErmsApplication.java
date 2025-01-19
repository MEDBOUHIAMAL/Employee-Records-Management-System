package com.example.ERMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ErmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErmsApplication.class, args);
	}

}
