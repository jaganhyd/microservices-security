package com.example.loans_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.loans_1.dto.LoansContactInfoDto;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = LoansContactInfoDto.class )
public class Loans1Application {

	public static void main(String[] args) {
		SpringApplication.run(Loans1Application.class, args);
	}

}
