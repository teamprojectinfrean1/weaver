package com.task.weaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WeaverApplication {
	public static void main(String[] args) {
		SpringApplication.run(WeaverApplication.class, args);
	}

}
