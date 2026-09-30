package com.example.cards_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.cards_1.dto.CardsContactInfoDto;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = CardsContactInfoDto.class )
public class Cards1Application {

	public static void main(String[] args) {
		SpringApplication.run(Cards1Application.class, args);
	}

}
