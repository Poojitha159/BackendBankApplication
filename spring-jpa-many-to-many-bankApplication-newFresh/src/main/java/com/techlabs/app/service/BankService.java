package com.techlabs.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.BankDTO;
import com.techlabs.app.entity.Bank;
import com.techlabs.app.repository.BankRepository;


public interface BankService {

	BankDTO getBankById(Long bankId);

	BankDTO addNewBank(BankDTO bankDTO);

	BankDTO updateBank(BankDTO bankDTO, Long bankId);

	List<BankDTO> getAllBanks();

	void deleteBankById(Long bankId);

	 

	 /*   public List<Bank> getAllBanks() {
	        return bankRepository.findAll();
	    }

	    public Bank saveBank(Bank bank) {
	        return bankRepository.save(bank);
	    }

	    public void deleteBank(Long id) {
	        bankRepository.deleteById(id);
	    }

		public Object getBankById(Long bankId) {
			// TODO Auto-generated method stub
			return null;
		}

		public Object addNewBank(BankDTO bankDTO) {
			// TODO Auto-generated method stub
			return null;
		}

		public void deleteBankById(Long bankId) {
			// TODO Auto-generated method stub
			
		}*/
}
