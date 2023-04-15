package com.baeker.baeker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BaekerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaekerApplication.class, args);
	}

}
