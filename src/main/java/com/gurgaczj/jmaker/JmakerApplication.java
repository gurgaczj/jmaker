package com.gurgaczj.jmaker;

import com.gurgaczj.jmaker.repository.AccountRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class JmakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmakerApplication.class, args);
	}
}
