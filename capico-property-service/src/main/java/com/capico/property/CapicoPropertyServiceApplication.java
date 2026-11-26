package com.capico.property;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CapicoPropertyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				CapicoPropertyServiceApplication.class,
				args
		);
	}
}