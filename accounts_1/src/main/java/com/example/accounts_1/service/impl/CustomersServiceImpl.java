package com.example.accounts_1.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.accounts_1.dto.AccountsDto;
import com.example.accounts_1.dto.CardsDto;
import com.example.accounts_1.dto.CustomerDetailsDto;
import com.example.accounts_1.dto.LoansDto;
import com.example.accounts_1.entity.Accounts;
import com.example.accounts_1.entity.Customer;
import com.example.accounts_1.exception.ResourceNotFoundException;
import com.example.accounts_1.mapper.AccountsMapper;
import com.example.accounts_1.mapper.CustomerMapper;
import com.example.accounts_1.repository.AccountsRepository;
import com.example.accounts_1.repository.CustomerRepository;
import com.example.accounts_1.service.ICustomersService;
import com.example.accounts_1.service.client.CardsFeignClient;
import com.example.accounts_1.service.client.LoansFeignClient;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
@AllArgsConstructor  //lombok will generate all argsConstr and autowire will happen automatically
public class CustomersServiceImpl implements ICustomersService {

	private AccountsRepository accountsRepository;
	private CustomerRepository customerRepository;
	private CardsFeignClient cardsFeignClient;
	private LoansFeignClient loansFeignClient;

	private RestTemplate restTemplate;

	/**
	 * @param mobileNumber  - Input Mobile Number
	 * @param correlationId - Correlation ID value generated at Edge server
	 * @return Customer Details based on a given mobileNumber
	 */
	@Override
	public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {

		CustomerDetailsDto customerDetailsDto = null;
		try {
			Customer customer = customerRepository.findByMobileNumber(mobileNumber)
					.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

			Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

			customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
			customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

			
			//Using feignClient
			ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
			if (null != loansDtoResponseEntity) {
				customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
			}

			ResponseEntity<CardsDto> cardsDtoResponseEntity = getCardsUsingFeignClient(mobileNumber, correlationId);


			if (null != cardsDtoResponseEntity) {
				customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerDetailsDto;

	}

	/*private void getCardsUsingRestTemplate(String mobileNumber, String correlationId) {


		restTemplate.getForObject("http://localhost:8072/jbmicroservice/cards-1/api/contact-info", HashMap.class);

	}*/

	private ResponseEntity<CardsDto> getCardsUsingFeignClient(String mobileNumber, String correlationId) {
		ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
		return cardsDtoResponseEntity;
	}
}
