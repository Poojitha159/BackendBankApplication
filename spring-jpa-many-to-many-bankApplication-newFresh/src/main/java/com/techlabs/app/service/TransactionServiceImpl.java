package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.exception.CustomerNotFoundException;
import com.techlabs.app.repository.AccountRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired 
    private AccountRepository accountRepository;
    
    @Autowired 
    private CustomerRepository customerRepository;
  
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
		super();
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
	}

	private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getDate(),
                transaction.getAccount().getId()
        );
    }

    private Transaction convertToEntity(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDTO.getId());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        return transaction;
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
    	
    	Account account=validateAccountStatus(accountId);
    	//Account account = accountRepository.findById(accountId).orElseThrow(()->new RuntimeException("Account not found"));
        List<Transaction> transactions = transactionRepository.findByAccount(account);
        return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TransactionDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return convertToDTO(transaction);
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByDateBetween(startDate, endDate);
        return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
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