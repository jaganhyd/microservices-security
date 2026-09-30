package com.example.accounts_1.service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.accounts_1.dto.CardsDto;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class CardsFallback implements CardsFeignClient {

    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
		return null;
    }

    @Override
    public ResponseEntity<CardsDto> createCard(String mobileNumber) {
        return null;
    }
}
