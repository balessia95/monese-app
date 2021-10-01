package com.monese.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.monese.web.dto.Transfer;
import com.monese.web.exception.TransferException;
import com.monese.web.model.Account;
import com.monese.web.model.Statement;
import com.monese.web.service.AccountService;
import com.monese.web.service.AccountStatementService;

@RestController
@RequestMapping("/api/v1")
public class BankController {
	private final AccountService accountService;
	private final AccountStatementService statementService;

	public BankController(AccountService accountService, AccountStatementService statementService) {
		this.accountService = accountService;
		this.statementService = statementService;
	}

	@PostMapping("/account/transfer")
	public ResponseEntity<?> transferFunds(@Valid @RequestBody Transfer amount) {
		try {
			Account account = accountService.transferMoney(amount.getFromAccountId(), amount.getToAccountId(), amount.getAmount());
			return new ResponseEntity<>(new HttpHeaders(), HttpStatus.CREATED);
		} catch (TransferException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/account/{id}/statement")
	public Statement getStatement(@PathVariable("id") int id) {
		try {
			return statementService.getAccountStatement(id);
		} catch (TransferException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
