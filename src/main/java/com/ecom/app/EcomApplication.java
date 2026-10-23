package com.ecom.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class EcomApplication {

//			TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

	public static void main(String[] args) {
		SpringApplication.run(EcomApplication.class, args);
	}

}
