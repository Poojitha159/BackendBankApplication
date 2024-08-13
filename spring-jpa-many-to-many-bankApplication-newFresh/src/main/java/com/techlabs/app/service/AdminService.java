package com.techlabs.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.util.PagedResponse;

public interface AdminService {

	

	AccountDTO saveAccount(AccountDTO accountDTO, Long customerId);

	//Page<Customer> getAllCustomers(Pageable peagable);

	CustomerDTO getCustomerById(Long id);

	PagedResponse<Customer> getAllCustomers(int page, int size, String sortBy, String direction);

	void deletAccount(Long accountId);

	PagedResponse<AccountDTO> getAccountsByCustomerId(Long customerId, int page, int size, String sortBy,
			String direction);

	void deletCustomer(Long customerId);

	

}
