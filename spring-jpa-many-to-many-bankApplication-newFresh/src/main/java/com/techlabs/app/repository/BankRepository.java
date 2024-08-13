package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank,Long>{

	


}
