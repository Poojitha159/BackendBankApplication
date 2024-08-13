package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>{



}
