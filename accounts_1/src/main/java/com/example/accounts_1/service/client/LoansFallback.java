package com.example.accounts_1.service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.accounts_1.dto.LoansDto;

@Component
public class LoansFallback implements LoansFeignClient {

	@Override
	public ResponseEntity<LoansDto> fetchLoanDetails(String correlationId, String mobileNumber) {
		return null;
	}

}
