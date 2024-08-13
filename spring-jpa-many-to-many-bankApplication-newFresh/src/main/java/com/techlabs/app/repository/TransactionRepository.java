package com.techlabs.app.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long>{

	List<Transaction> findByAccount(Account account);

	//Page<Transaction> findByCustomerId(Long customerId, Pageable pageable);


	Page<Transaction> findByAccountId(Long id, Pageable pageable);

	//List<Transaction> findByCustomerIdDateBetween(Long customerId, LocalDateTime startDate, LocalDateTime endDate);

	List<Transaction> findByAccountId(Long id);

	Page<Transaction> findByAccountIdIn(List<Long> accountIds, Pageable pageable);

	Page<Transaction> findByAccountIdInAndDateBetween(List<Long> accountIds, LocalDateTime startDate,
			LocalDateTime endDate, Pageable pageable);

	List<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	
}
