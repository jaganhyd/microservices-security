package com.example.accounts_1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.accounts_1.dto.CustomerDetailsDto;
import com.example.accounts_1.service.ICustomersService;

import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class CustomerController { //

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private final ICustomersService iCustomersService;
	public CustomerController(ICustomersService iCustomersService) { //constructor injection
		this.iCustomersService = iCustomersService;
	}

	@GetMapping("/fetchCustomerDetails")  //API composition pattern with feign client
	public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestHeader("jbmicroservice-correlation-id") String correlationId,
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {

		logger.debug("fetchCustomerDetails method start with correlationId jbmicroservice-correlation-id - {}", correlationId);
		CustomerDetailsDto customerDetailsDto = iCustomersService.fetchCustomerDetails(mobileNumber, correlationId);

		logger.debug("fetchCustomerDetails method end");
		return ResponseEntity.status(HttpStatus.OK).body(customerDetailsDto);

	}

}
