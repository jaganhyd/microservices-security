package com.example.accounts_1.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.accounts_1.dto.CardsDto;

@FeignClient(name="cards-1" , fallback = CardsFallback .class) //same name which is registered in eureka server
public interface CardsFeignClient {
	
	@GetMapping(value="/api/fetch", consumes="application/json")
	public ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader("jbmicroservice-correlation-id") String correlationId, @RequestParam  String mobileNumber);

	@PostMapping(value="/api/create", consumes="application/json")
	public ResponseEntity<CardsDto> createCard(@RequestParam  String mobileNumber);

}
