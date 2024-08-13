package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.techlabs.app.dto.TransactionDTO;

public interface TransactionService {

	List<TransactionDTO> getTransactionsByAccountId(Long accountId);

	TransactionDTO getTransactionById(Long transactionId);


	

	void deleteTransaction(Long transactionId);

	List<TransactionDTO> getAllTransactions();


	List<TransactionDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

}
