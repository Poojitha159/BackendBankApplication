package com.techlabs.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.BankDTO;
import com.techlabs.app.entity.Bank;
import com.techlabs.app.exception.BankNotFoundException;
import com.techlabs.app.repository.BankRepository;



@Service

public class BankServiceImpl implements BankService{
	
    @Autowired
    private BankRepository bankRepository;

	@Override
	public BankDTO getBankById(Long bankId) {
		Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new BankNotFoundException("Bank not found"));
        return mapToDTO(bank);
	}

	@Override
	public BankDTO addNewBank(BankDTO bankDTO) {
		Bank bank = new Bank();
        bank.setFullName(bankDTO.getFullName());
        bank.setAbbreviation(bankDTO.getAbbreviation());
        Bank savedBank = bankRepository.save(bank);
        return mapToDTO(savedBank);}

	@Override
	public BankDTO updateBank(BankDTO bankDTO, Long bankId) {
		 Bank bank = bankRepository.findById(bankId)
	                .orElseThrow(() -> new BankNotFoundException("Bank not found"));

	        bank.setFullName(bankDTO.getFullName());
	        bank.setAbbreviation(bankDTO.getAbbreviation());

	        Bank updatedBank = bankRepository.save(bank);
	        return mapToDTO(updatedBank);
	   
	}

	@Override
	public List<BankDTO> getAllBanks() {
		List<Bank> banks = bankRepository.findAll();
        return banks.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

	@Override
	public void deleteBankById(Long bankId) {
	        Bank bank = bankRepository.findById(bankId)
	                .orElseThrow(() -> new BankNotFoundException("Bank not found"));
	        bankRepository.delete(bank);
	}
	
	private BankDTO mapToDTO(Bank bank) {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setId(bank.getId());  // assuming BankDTO has an id field
        bankDTO.setFullName(bank.getFullName());
        bankDTO.setAbbreviation(bank.getAbbreviation());
        return bankDTO;
    }
}
