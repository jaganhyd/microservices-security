package com.example.accounts_1.controller;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.accounts_1.constants.AccountsConstants;
import com.example.accounts_1.dto.AccountsContactInfoDto;
import com.example.accounts_1.dto.CustomerDto;
import com.example.accounts_1.dto.ResponseDto;
import com.example.accounts_1.service.IAccountsService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class AccountsController {

	private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

	private final IAccountsService iAccountsService;

	public AccountsController(IAccountsService iAccountsService) {
		this.iAccountsService = iAccountsService;
	}

	@Value("${build.version}")
	private String buildVersion;

	@Autowired
	private Environment environment;

	@Autowired
	private AccountsContactInfoDto accountsContactInfoDto;

	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
		iAccountsService.createAccount(customerDto);
		ResponseDto rd = new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201);
		return ResponseEntity.status(HttpStatus.CREATED).body(rd);
	}

	@GetMapping("/fetch")
	public ResponseEntity<CustomerDto> fetchAccountDetails(
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	}

	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
		boolean isUpdated = iAccountsService.updateAccount(customerDto);
		if (isUpdated) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDto> deleteAccountDetails(
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
		if (isDeleted) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
		}
	}

	@Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() throws TimeoutException {
		logger.debug("getBuildInfo() method Invoked #############################################");
		//Test For retry
		/*try {
			synchronized (this) {
				wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		//throw new NullPointerException();   ignoreExceptions
		
		throw new TimeoutException();  //retryExceptions
		
		//logger.debug("getBuildInfo() method Invoked");
		//return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
	}
	
	
	public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
		
		logger.debug("getBuildInfoFallback() method Invoked #############################################");
		return ResponseEntity.status(HttpStatus.OK).body("0.9");
	}

	@RateLimiter(name= "getJavaVersion", fallbackMethod ="getJavaVersionFallback")
	@GetMapping("/java-version") // ENV veriable
	public ResponseEntity<String> getJavaVersion() {
		//return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
		return ResponseEntity.status(HttpStatus.OK).body("Java - 8");
	}

	@GetMapping("/contact-info")
	public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
		
		//Test For CircuitBreaker
		/*try {
			synchronized (this) {
				wait(6000000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(accountsContactInfoDto);
	}

	public ResponseEntity<String> getJavaVersionFallback(Throwable throwable) {
		return ResponseEntity.status(HttpStatus.OK).body("Java 17");
	}
	
	
	/*
	 * public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
	 * logger.debug("getBuildInfoFallback() method Invoked"); return ResponseEntity
	 * .status(HttpStatus.OK) .body("0.9"); }
	 * 
	 * 
	 * 
	 */

}
