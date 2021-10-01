package com.monese.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monese.web.model.Account;
import com.monese.web.model.Statement;

@Service
public class AccountStatementService {

    @Autowired
    AccountService accountService;

    public AccountStatementService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Statement getAccountStatement(long id){
        Account account = accountService.getAccountById(id);
        Statement statement = new Statement();
        statement.setAccountId(account.getId());
        statement.setBalance(account.getBalance());
        statement.setTransactions(account.getTransactions());
        return statement;
    }

}