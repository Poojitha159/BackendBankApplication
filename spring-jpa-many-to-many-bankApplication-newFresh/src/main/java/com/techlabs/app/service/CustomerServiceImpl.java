package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Bank;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.CustomerNotFoundException;
import com.techlabs.app.repository.AccountRepository;
import com.techlabs.app.repository.BankRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.TransactionRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private BankRepository bankRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private UserRepository userRepository;

	public CustomerServiceImpl(CustomerRepository customerRepository, BankRepository bankRepository,
			AccountRepository accountRepository, TransactionRepository transactionRepository,
			UserRepository userRepository) {
		super();
		this.customerRepository = customerRepository;
		this.bankRepository = bankRepository;
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void deposit(Long accountId, double amount) {
		/*Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found"));

		account.setBalance(account.getBalance() + amount);
		account.getCustomer().setTotalBalance(account.getCustomer().getTotalBalance() + amount);
		Transaction transaction = new Transaction(amount, "DEPOSIT", LocalDateTime.now(), account);
		account.getTransactions().add(transaction);
		accountRepository.save(account);*/
		 Account account = validateAccountStatus(accountId);
	        account.setBalance(account.getBalance() + amount);
	        account.getCustomer().setTotalBalance(account.getCustomer().getTotalBalance() + amount);
			Transaction transaction = new Transaction(amount, "DEPOSIT", LocalDateTime.now(), account);
			account.getTransactions().add(transaction);
			accountRepository.save(account);
	}

	@Override
	public void withdraw(Long accountId, double amount) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found"));

		if (account.getBalance() < amount) {
			throw new RuntimeException("Insufficient funds");
		}

		account.setBalance(account.getBalance() - amount);

		account.getCustomer().setTotalBalance(account.getCustomer().getTotalBalance() - amount);

		Transaction transaction = new Transaction(amount, "WITHDRAW", LocalDateTime.now(), account);
		account.getTransactions().add(transaction);

		accountRepository.save(account);
	}

	@Override

	public void transfer(Long fromAccountId, Long toAccountId, double amount) {
		
		Account fromAccount = validateAccountStatus(fromAccountId);
		Account toAccount = validateAccountStatus(toAccountId);
		/*Account fromAccount = accountRepository.findById(fromAccountId)
				.orElseThrow(() -> new RuntimeException("Source account not found"));

		Account toAccount = accountRepository.findById(toAccountId)
				.orElseThrow(() -> new RuntimeException("Destination account not found"));
*/
		if (fromAccount.getBalance() < amount) {
			throw new RuntimeException("Insufficient funds in source account");
		}

		fromAccount.setBalance(fromAccount.getBalance() - amount);
		toAccount.setBalance(toAccount.getBalance() + amount);

		fromAccount.getCustomer().setTotalBalance(fromAccount.getCustomer().getTotalBalance() - amount);
		toAccount.getCustomer().setTotalBalance(toAccount.getCustomer().getTotalBalance() + amount);

		Transaction transaction = new Transaction(amount, "WITHDRAW", LocalDateTime.now(), fromAccount);
		Transaction transaction1 = new Transaction(amount, "DEPOSIT", LocalDateTime.now(), toAccount);

		fromAccount.getTransactions().add(transaction);
		toAccount.getTransactions().add(transaction1);

		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
	}

	private CustomerDTO convertToDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setId(customer.getId());
		customerDTO.setFirstName(customer.getFirstName());
		customerDTO.setLastName(customer.getLastName());
		customerDTO.setActive(customer.isActive());
		customerDTO.setTotalBalance(customer.getTotalBalance());
		// Include other fields as needed
		return customerDTO;
	}

	private Customer convertToEntity(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		customer.setId(customerDTO.getId());
		customer.setFirstName(customerDTO.getFirstName());
		customer.setLastName(customerDTO.getLastName());
		customer.setActive(customerDTO.isActive());
		customer.setTotalBalance(customerDTO.getTotalBalance());
		// Include other fields as needed
		return customer;
	}

	@Override
	public double getTotalBalance(Long customerId) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));
		return customer.getAccounts().stream().mapToDouble(Account::getBalance).sum();
	}

	@Override
	public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found"));
		customer.setFirstName(customerDTO.getFirstName());
		customer.setLastName(customerDTO.getLastName());
		customer.setActive(customerDTO.isActive());
//customer.setTotalBalance(customerDTO.getTotalBalance());
		Customer updatedCustomer = customerRepository.save(customer);
		return convertToDTO(updatedCustomer);
	}

	@Override
	public Double getAccountBalance(Long accountId) {
		
		Account account=validateAccountStatus(accountId);
		if(account.equals(accountId)) {
		return account.getBalance();
		
		}
		else{
			return null;
		}
			//return accountRepository.findById(accountId).map(Account::getBalance) 
		
			//	.orElse(null); // Return null if account not found
	}

	@Override
	public PagedResponse<TransactionDTO> getCustomerTransactions(Long customerId, int page, int size, String sortBy,
			String direction) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new RuntimeException("User not found with username or email: " + usernameOrEmail));

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		if (!customer.getUser().equals(user)) {
			throw new RuntimeException("The logged user is different from fetched user");
		}
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		List<Long> accountIds = accountRepository.findByCustomer(customer).stream().map(Account::getId)
				.collect(Collectors.toList());

		Page<Transaction> transactionPage = transactionRepository.findByAccountIdIn(accountIds, pageable);

		List<TransactionDTO> transactionDTOs = convertListToDTO(transactionPage.getContent());

		return new PagedResponse<>(transactionDTOs, page, size, transactionPage.getTotalElements(),
				transactionPage.getTotalPages(), transactionPage.isLast());
	}

	private List<TransactionDTO> convertListToDTO(List<Transaction> transactions) {
		List<TransactionDTO> transactionDTOs = new ArrayList<>();
		for (Transaction transaction : transactions) {
			TransactionDTO dto = new TransactionDTO();
			dto.setId(transaction.getId());
			dto.setAmount(transaction.getAmount());
			// dto.setDate(transaction.getDate());
			dto.setDate(transaction.getDate());
			dto.setTransactionType(transaction.getTransactionType());
			dto.setAccountId(transaction.getAccount().getId());

			// dto.setAccountNumber(transaction.getAccount().getId());
			// Add other fields as needed
			transactionDTOs.add(dto);
		}
		return transactionDTOs;
	}

	@Override
	public PagedResponse<TransactionDTO> getCustomerTransactionsByDate(Long customerId, LocalDateTime startDate,
			LocalDateTime endDate, int page, int size, String sortBy, String direction) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new RuntimeException("User not found with username or email: " + usernameOrEmail));

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		if (!customer.getUser().equals(user)) {
			throw new RuntimeException("The logged user is different from fetched user");
		}
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		List<Long> accountIds = accountRepository.findByCustomer(customer).stream().map(Account::getId)
				.collect(Collectors.toList());

		Page<Transaction> transactionPage = transactionRepository.findByAccountIdInAndDateBetween(accountIds, startDate,
				endDate, pageable);

		List<TransactionDTO> transactionDTOs = convertListToDTO(transactionPage.getContent());

		return new PagedResponse<>(transactionDTOs, page, size, transactionPage.getTotalElements(),
				transactionPage.getTotalPages(), transactionPage.isLast());
	}

	private TransactionDTO convertToDTO(Transaction transaction) {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setId(transaction.getId());
		transactionDTO.setAccountId(transaction.getAccount().getId());
		transactionDTO.setAmount(transaction.getAmount());
		transactionDTO.setDate(transaction.getDate());

		transactionDTO.setTransactionType(transaction.getTransactionType());

		return transactionDTO;
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
