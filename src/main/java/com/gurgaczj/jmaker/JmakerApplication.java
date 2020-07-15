package com.gurgaczj.jmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class JmakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmakerApplication.class, args);
	}

}
