package com.techlabs.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.BankDTO;
import com.techlabs.app.entity.Bank;
import com.techlabs.app.service.BankService;




@RestController
@RequestMapping("/api/banks")
public class BankController {
	
	    @Autowired
	    private BankService bankService;

	    @GetMapping("/{bankId}")
	    public ResponseEntity<BankDTO> getBankById(@PathVariable Long bankId) {
	        return ResponseEntity.ok(bankService.getBankById(bankId));
	    }

	    @PostMapping
	    public ResponseEntity<BankDTO> addNewBank(@RequestBody BankDTO bankDTO) {
	        return ResponseEntity.ok(bankService.addNewBank(bankDTO));
	    }

	    @PutMapping("/{bankId}")
	    public ResponseEntity<BankDTO> updateBank(@PathVariable Long bankId, @RequestBody BankDTO bankDTO) {
	        return ResponseEntity.ok(bankService.updateBank(bankDTO, bankId));
	    }

	    @GetMapping("/all")
	    public ResponseEntity<List<BankDTO>> getAllBanks() {
	        return ResponseEntity.ok(bankService.getAllBanks());
	    }
	    @DeleteMapping("/{bankId}")
	    public ResponseEntity<Void> deleteBankById(@PathVariable Long bankId) {
	        bankService.deleteBankById(bankId);
	        return ResponseEntity.noContent().build();
	    }
	}

