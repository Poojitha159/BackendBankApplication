package com.techlabs.app.dto;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Transaction;

public class TransactionDTO {
	private Long id;
	private double amount;
	private String transactionType;

	private LocalDateTime date;
	private Long accountId;
	
	

	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	

	public TransactionDTO(Long id, double amount, String transactionType, LocalDateTime date, Long accountId) {
		super();
		this.id = id;
		this.amount = amount;
		this.transactionType = transactionType;
		this.date = date;
		this.accountId = accountId;
	}
	public TransactionDTO(Page<Transaction> page1) {
		
	}
	public TransactionDTO() {
		
	}
	public TransactionDTO(Long id2, double amount2, String transactionType2, LocalDateTime date2, Account account) {
		this.id = id2;
		this.amount = amount2;
		this.transactionType = transactionType2;
		this.date = date2;
		this.accountId = accountId;
	}


}
