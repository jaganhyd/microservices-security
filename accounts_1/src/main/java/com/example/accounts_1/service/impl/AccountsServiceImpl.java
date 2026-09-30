package com.example.accounts_1.service.impl;

import com.example.accounts_1.constants.AccountsConstants;
import com.example.accounts_1.dto.AccountsDto;
import com.example.accounts_1.dto.CardsDto;
import com.example.accounts_1.dto.CustomerDto;
import com.example.accounts_1.entity.Accounts;
import com.example.accounts_1.entity.Customer;
import com.example.accounts_1.exception.CustomerAlreadyExistsException;
import com.example.accounts_1.exception.ResourceNotFoundException;
import com.example.accounts_1.mapper.AccountsMapper;
import com.example.accounts_1.mapper.CustomerMapper;
import com.example.accounts_1.repository.AccountsRepository;
import com.example.accounts_1.repository.CustomerRepository;
import com.example.accounts_1.service.IAccountsService;
import com.example.accounts_1.service.client.CardsFeignClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor  //autowire will happen automatically with variables defined
public class AccountsServiceImpl implements IAccountsService {

	private static final Logger logger = LoggerFactory.getLogger(AccountsServiceImpl.class);

	private AccountsRepository accountsRepository;
	private CustomerRepository customerRepository;
	private CardsFeignClient cardsFeignClient;
	//private final StreamBridge streamBridge;

	private RestTemplate restTemplate;

	@Override
	public void createAccount(CustomerDto customerDto) {
		Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
		Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());

		if (optionalCustomer.isPresent()) {
			throw new CustomerAlreadyExistsException(
					"Customer already registered with given mobileNumber " + customerDto.getMobileNumber());
		}
		Customer savedCustomer = customerRepository.save(customer);
		Accounts savedAccount = accountsRepository.save(createNewAccount(savedCustomer));
		System.out.println();

		createCardUsingFeignClient(customerDto);
		getCardUsingRestTemplate(customerDto);

		//RabbitMQ/KAFKA
		//sendCommunication(savedAccount, savedCustomer);
	}

	private void createCardUsingFeignClient(CustomerDto customerDto) {
		ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.createCard(customerDto.getMobileNumber());
		System.out.println(cardsDtoResponseEntity.getBody().getCardNumber());
	}

	private void getCardUsingRestTemplate(CustomerDto customerDto) {
		//To get Single object
		//restTemplate.getForEntity()
		//restTemplate.getForObject()
		CardsDto cardsDto = restTemplate.getForObject("CARDS-1/api/fetchCardDetail/"+customerDto.getMobileNumber(), CardsDto.class);

		System.out.println(cardsDto.getCardNumber());
	}

	/*private void sendCommunication(Accounts account, Customer customer) {
		var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(), customer.getName(), customer.getEmail(), customer.getMobileNumber());

		logger.info("Sending Communication request for the details: {}", accountsMsgDto);

		var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);  //"sendCommunication-out-0" -> messg exchange

		logger.info("Is the Communication request successfully triggered ? : {}", result);
	}*/

	private Accounts createNewAccount(Customer customer) {
		Accounts newAccount = new Accounts();
		newAccount.setCustomerId(customer.getCustomerId());
		long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

		newAccount.setAccountNumber(randomAccNumber);
		newAccount.setAccountType(AccountsConstants.SAVINGS);
		newAccount.setBranchAddress(AccountsConstants.ADDRESS);
		return newAccount;
	}

	@Override
	public CustomerDto fetchAccount(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
				.orElseThrow(
						() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

		CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
		customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
		return customerDto;
	}

	/**
	 * @param customerDto - CustomerDto Object
	 * @return boolean indicating if the update of Account details is successful or not
	 */
	@Override
	public boolean updateAccount(CustomerDto customerDto) {
		boolean isUpdated = false;
		AccountsDto accountsDto = customerDto.getAccountsDto();
		if (accountsDto != null) {
			Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber())
					.orElseThrow(() -> new ResourceNotFoundException("Account", "AccountNumber",
							accountsDto.getAccountNumber().toString()));

			AccountsMapper.mapToAccounts(accountsDto, accounts);
			accounts = accountsRepository.save(accounts);

			Long customerId = accounts.getCustomerId();
			Customer customer = customerRepository.findById(customerId)
					.orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString()));

			CustomerMapper.mapToCustomer(customerDto, customer);
			customerRepository.save(customer);
			isUpdated = true;
		}
		return isUpdated;
	}

	/**
	 * @param mobileNumber - Input Mobile Number
	 * @return boolean indicating if the delete of Account details is successful or not
	 */
	@Override
	public boolean deleteAccount(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
		
		accountsRepository.deleteByCustomerId(customer.getCustomerId());
		customerRepository.deleteById(customer.getCustomerId());
		return true;
	}

	/**
	 * @param accountNumber - Long
	 * @return boolean indicating if the update of communication status is successful or not
	 */
	@Override
	public boolean updateCommunicationStatus(Long accountNumber) {
		boolean isUpdated = false;
		
		if (accountNumber != null) {
			Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
					() -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString()));
			
			accounts.setCommunicationSw(true);
			accountsRepository.save(accounts);
			isUpdated = true;
		}
		return isUpdated;
	}

}
