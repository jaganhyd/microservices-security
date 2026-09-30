package com.example.message.functions;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.message.dto.AccountsMsgDto;

@Configuration
public class MessageFunctions {

	private static final Logger logger = LoggerFactory.getLogger(MessageFunctions.class);

	//Input of type AccountsMsgDto, output of type AccountsMsgDto
	@Bean
	public Function<AccountsMsgDto, AccountsMsgDto> email() {
		return accountsMsgDto -> {
			logger.info("Sending email with the details : " + accountsMsgDto.toString());
			return accountsMsgDto;
		};
	}

	//Output of email, will be used in this sms, this is to ACK accounts MS
	@Bean   //for spring cloud function to identify
	public Function<AccountsMsgDto, Long> sms() {
		return accountsMsgDto -> {
			logger.info("Sending sms with the details : " + accountsMsgDto.toString());
			return accountsMsgDto.accountNumber();
		};
	}
}
