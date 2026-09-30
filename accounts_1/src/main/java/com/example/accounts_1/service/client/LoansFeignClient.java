package com.example.accounts_1.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.accounts_1.dto.LoansDto;

@FeignClient(name = "loans-1", fallback = LoansFallback.class) //same name which is registered in eureka server
public interface LoansFeignClient {
	
	@GetMapping(value="/api/fetch", consumes="application/json")
	public ResponseEntity<LoansDto> fetchLoanDetails(
			@RequestHeader("jbmicroservice-correlation-id") String correlationId, 
			@RequestParam  String mobileNumber);

}
