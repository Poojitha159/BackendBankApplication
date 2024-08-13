package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.util.PagedResponse;

public interface CustomerService {



	void deposit(Long accountId, double amount);

	void withdraw(Long accountId, double amount);

	void transfer(Long fromAccountId, Long toAccountId, double amount);

	double getTotalBalance(Long customerId);

	CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

	Double getAccountBalance(Long accountId);


	PagedResponse<TransactionDTO> getCustomerTransactions(Long customerId, int page, int size, String sortBy,
			String direction);


	PagedResponse<TransactionDTO> getCustomerTransactionsByDate(Long customerId, LocalDateTime startDate,
			LocalDateTime endDate, int page, int size, String sortBy, String direction);


	//List<Customer> getAllCustomers();

	//Optional<Customer> getCustomerById(Long id);

}
