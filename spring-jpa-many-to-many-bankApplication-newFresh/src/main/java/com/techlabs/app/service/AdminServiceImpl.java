package com.techlabs.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Bank;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.exception.CustomerNotFoundException;
import com.techlabs.app.repository.AccountRepository;
import com.techlabs.app.repository.BankRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.util.PagedResponse;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BankRepository bankRepository;

	@Autowired
	private CustomerRepository customerRepository;
	
	private EmailService emailService;
	
	

	
	                  

	public AdminServiceImpl(AccountRepository accountRepository, BankRepository bankRepository,
			CustomerRepository customerRepository, EmailService emailService) {
		super();
		this.accountRepository = accountRepository;
		this.bankRepository = bankRepository;
		this.customerRepository = customerRepository;
		this.emailService = emailService;
	}

	@Override
	public AccountDTO saveAccount(AccountDTO accountDTO, Long customerId) {
	
		Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Bank bank=bankRepository.findById(accountDTO.getBankId())
        		.orElseThrow(()->new RuntimeException("Bank not found"));

        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setBalance(accountDTO.getBalance());
        
        account.setBank(bank);
        System.out.println(account);

        Account savedAccount = accountRepository.save(account);
        
        String subject = "Creation of account";
        String text = "Account created successfully with " + savedAccount.getAccountNumber() + "and Balance is "
            + savedAccount.getBalance() + " and customer id is " + savedAccount.getCustomer().getId();
        emailService.sendMail(savedAccount.getCustomer().getUser().getEmail(), subject, text);
        
        return convertToDTO(savedAccount);
    
	}

	
	@Override
	public CustomerDTO getCustomerById(Long id) {
		
		Customer customer=validateCustomerStatus(id);
		return convertToDTO(customer);
	//	Customer byId = customerRepository.findById(id).orElseThrow(()->new RuntimeException("customer not found"));
	
		//return convertToDTO(byId);
		 //return customerRepository.findById(id).orElseThrow(()->new RuntimeException("custo"));
	}
	
	
	private CustomerDTO convertToDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setId(customer.getId());
		customerDTO.setFirstName(customer.getFirstName());
		customerDTO.setLastName(customer.getLastName());
		customerDTO.setTotalBalance(customer.getTotalBalance());
		// Include other fields as needed
		return customerDTO;
	}

	private Customer convertToEntity(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		customer.setId(customerDTO.getId());
		customer.setFirstName(customerDTO.getFirstName());
		customer.setLastName(customerDTO.getLastName());
		customer.setTotalBalance(customerDTO.getTotalBalance());
		return customer;
	}
	
	
	private AccountDTO convertToDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setAccountNumber(account.getAccountNumber());
        accountDTO.setBankId(account.getBank().getId());
        return accountDTO;
    }

    private Account convertToEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setBalance(accountDTO.getBalance());
        account.setAccountNumber(accountDTO.getAccountNumber());
        
        return account;
    }

	@Override
	public PagedResponse<Customer> getAllCustomers(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		  Pageable pegeable= PageRequest.of(page, size,sort);
		  Page<Customer> page1=customerRepository.findAll(pegeable);
		  List<Customer> customers=page1.getContent();
		
		  return new PagedResponse<Customer>(customers, page1.getNumber(),page1.getSize(),page1.getTotalElements(),page1.getTotalPages(),page1.isLast()); 
	}

	@Override
	public void deletAccount(Long accountId) {
		
		Account account=validateAccountStatus(accountId);
		account.setActive(false);
		accountRepository.save(account);

		/*Account account=accountRepository.findById(accountId)
				.orElseThrow(()->new RuntimeException("Account not found"));
		accountRepository.delete(account);*/
	}



	@Override
	public PagedResponse<AccountDTO> getAccountsByCustomerId(Long customerId, int page, int size, String sortBy,
			String direction) {
		
		Customer customer = customerRepository.findById(customerId)
	            .orElseThrow(() -> new RuntimeException("Customer id"+ customerId));

	        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) 
	                    ? Sort.by(sortBy).descending() 
	                    : Sort.by(sortBy).ascending();
	        Pageable pageable = PageRequest.of(page, size, sort);

	        Page<Account> accountsPage = accountRepository.findByCustomer(customer, pageable);

	        List<Account> accounts=accountsPage.getContent();
	        List<AccountDTO> accountDTOs =convertListToDTO(accounts); 
	        return new PagedResponse<>(accountDTOs, accountsPage.getNumber(),
	                accountsPage.getSize(), accountsPage.getTotalElements(),
	                accountsPage.getTotalPages(), accountsPage.isLast());
	    }
	

		private List<AccountDTO> convertListToDTO(List<Account> accounts) {
			List<AccountDTO> accountDTOs = new ArrayList<>();
			for (Account account : accounts) {
				AccountDTO dto = new AccountDTO();
				dto.setId(account.getId());
				dto.setAccountNumber(account.getAccountNumber());
				dto.setBalance(account.getBalance());
				dto.setBankId(account.getBank().getId());
				accountDTOs.add(dto);
			}
			return accountDTOs;
		}

		@Override
		public void deletCustomer(Long customerId) {
			Customer customer=validateCustomerStatus(customerId);
			customer.setActive(false);
			customerRepository.save(customer);
		
			/*Customer customer=customerRepository.findById(customerId)
					.orElseThrow(()->new RuntimeException("Customer not found"));
			customerRepository.delete(customer);*/
		}
		
		private Customer validateCustomerStatus(Long customerId) {
			return customerRepository.findById(customerId).filter(Customer::isActive)
					.orElseThrow(() -> new CustomerNotFoundException("Customer not found or inactive."));
		}

		private Account validateAccountStatus(Long accountId) {
			return accountRepository.findById(accountId).filter(Account::isActive)
					.orElseThrow(() -> new CustomerNotFoundException("Account not found or inactive."));
		}
}
