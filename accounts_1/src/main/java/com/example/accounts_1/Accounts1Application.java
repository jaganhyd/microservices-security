package com.example.accounts_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.accounts_1.dto.AccountsContactInfoDto;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = AccountsContactInfoDto.class )//commenting for docker without configserver
@EnableAspectJAutoProxy
@EnableFeignClients
public class Accounts1Application {

	public static void main(String[] args) {
		SpringApplication.run(Accounts1Application.class, args);
	}

}
