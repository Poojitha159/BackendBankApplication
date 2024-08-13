package com.techlabs.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.util.PagedResponse;

@RestController
@RequestMapping("/api/accounts")

public class AdminController {

	// @Autowired
	// private AccountService accountService;

	// @Autowired
	// private CustomerService customerService;
	@Autowired
	private AdminService adminService;

	public AdminController(AdminService adminService) {
		super();
		this.adminService = adminService;
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<PagedResponse<AccountDTO>> getAccountsByCustomerId(@PathVariable Long customerId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sort", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<AccountDTO> ps=adminService.getAccountsByCustomerId(customerId, page, size, sortBy, direction);
		return new ResponseEntity<PagedResponse<AccountDTO>>(ps,HttpStatus.OK);
		//return adminService.getAccountsByCustomerId(customerId, page, size, sortBy, direction);
	}

	/*
	 * @GetMapping("/{id}") public ResponseEntity<AccountDTO>
	 * getAccountById(@PathVariable Long id) { Optional<AccountDTO> accountDTO =
	 * accountService.getAccountById(id); return
	 * accountDTO.map(ResponseEntity::ok).orElseGet(() ->
	 * ResponseEntity.notFound().build()); }
	 */
	@PostMapping("/{customerId}")
	public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO, @PathVariable Long customerId) {
		AccountDTO createdAccount = adminService.saveAccount(accountDTO, customerId);
		return ResponseEntity.ok(createdAccount);
	}

	@GetMapping
	public ResponseEntity<PagedResponse<Customer>> getAllCustomers(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sort", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<Customer> cus = adminService.getAllCustomers(page, size, sortBy, direction);
		return new ResponseEntity<PagedResponse<Customer>>(cus, HttpStatus.OK);

		// return adminService.getAllCustomers(peagable);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
		CustomerDTO customer = adminService.getCustomerById(id);
		return new ResponseEntity<CustomerDTO>(customer, HttpStatus.OK);
		// return customer.map(ResponseEntity::ok).orElseGet(() ->
		// ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{accountId}")
	public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
		adminService.deletAccount(accountId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/deletecustomer/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
		adminService.deletCustomer(customerId);
		return ResponseEntity.noContent().build();
	}
	
	
}
