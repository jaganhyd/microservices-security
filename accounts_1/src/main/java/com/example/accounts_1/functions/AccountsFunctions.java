package com.example.accounts_1.functions;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.accounts_1.service.IAccountsService;

@Configuration
public class AccountsFunctions {

	
	private static final Logger logger = LoggerFactory.getLogger(AccountsFunctions.class);


	/*is responsible to accept the message from the message microservice.*/

	@Bean
	public Consumer<Long> updateCommunication (IAccountsService accountsService) {
		return accountNumber -> {
			logger.info("Updating communication - "+ accountNumber.toString());
			accountsService.updateCommunicationStatus(accountNumber);
			System.out.println();
		};
	}
}
