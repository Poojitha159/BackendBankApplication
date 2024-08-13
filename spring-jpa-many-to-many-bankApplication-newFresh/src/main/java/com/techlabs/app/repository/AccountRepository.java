package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Customer;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{

	Page<Account> findByCustomer(Customer customer, Pageable pageable);


	List<Account> findByCustomer(Customer customer);

}
