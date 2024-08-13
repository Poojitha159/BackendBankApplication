package com.techlabs.app.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.util.PagedResponse;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping("/{customerId}/passbook")
	public ResponseEntity<PagedResponse<TransactionDTO>> getCustomerPassbook(@PathVariable Long customerId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sort", defaultValue = "date") String sortBy,
			@RequestParam(name = "direction", defaultValue = "desc") String direction) {
		PagedResponse<TransactionDTO> transactions = customerService.getCustomerTransactions(customerId, page, size,
				sortBy, direction);

		return new ResponseEntity<PagedResponse<TransactionDTO>>(transactions, HttpStatus.OK);

	}

	@GetMapping("/{customerId}/transactionsByDates")
	public ResponseEntity<PagedResponse<TransactionDTO>> getCustomerTransactions(@PathVariable Long customerId,
			@RequestParam(name = "startDate") LocalDateTime startDate,
			@RequestParam(name = "endDate") LocalDateTime endDate,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sort", defaultValue = "date") String sortBy,
			@RequestParam(name = "direction", defaultValue = "desc") String direction) {

		PagedResponse<TransactionDTO> transactions = customerService.getCustomerTransactionsByDate(customerId,
				startDate, endDate, page, size, sortBy, direction);
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	@PostMapping("/{accountId}/deposit")
	public ResponseEntity<Void> deposit(@PathVariable Long accountId, @RequestParam double amount) {
		customerService.deposit(accountId, amount);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{accountId}/withdraw")
	public ResponseEntity<Void> withdraw(@PathVariable Long accountId, @RequestParam double amount) {
		customerService.withdraw(accountId, amount);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{fromAccountId}/transfer/{toAccountId}")
	public ResponseEntity<Void> transfer(@PathVariable Long fromAccountId, @PathVariable Long toAccountId,
			@RequestParam double amount) {
		customerService.transfer(fromAccountId, toAccountId, amount);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{customerId}/totalBalance")
	public ResponseEntity<Double> getTotalBalance(@PathVariable Long customerId) {
		double totalBalance = customerService.getTotalBalance(customerId);
		return ResponseEntity.ok(totalBalance);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
		CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
		return ResponseEntity.ok(updatedCustomer);
	}

	@GetMapping("/{accountId}/balance")
	public ResponseEntity<Double> getAccountBalance(@PathVariable Long accountId) {
		Double balance = customerService.getAccountBalance(accountId);
		if (balance != null) {
			return ResponseEntity.ok(balance);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
