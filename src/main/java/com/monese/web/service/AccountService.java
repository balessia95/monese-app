package com.monese.web.service;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.monese.web.exception.TransferException;
import com.monese.web.model.Account;
import com.monese.web.model.Transaction;
import com.monese.web.repository.AccountsRepository;
import com.monese.web.repository.TransactionsRepository;

@Service
@Transactional
public class AccountService {
    public static final String TRANSACTION_TYPE_CREDIT = "CREDIT";
    public static final String TRANSACTION_TYPE_DEBIT = "DEBIT";

    private final AccountsRepository accountRepository;
    private final TransactionsRepository transactionRepository;

    public AccountService(AccountsRepository accountRepository, TransactionsRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public synchronized Account deposit(long id, BigDecimal amount) {
        Account account = getAccountById(id);
        try {

            account.setBalance(account.getBalance().add(amount));
            account = accountRepository.save(account);
            Transaction transaction = createTransaction(amount, account, TRANSACTION_TYPE_CREDIT);
            account.addTransaction(transaction);
            accountRepository.save(account);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return account;
    }

    private Transaction createTransaction(BigDecimal amount, Account account, String transactionTypeCredit) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setBalance(account.getBalance());
        transaction.setType(transactionTypeCredit);
        return transaction;
    }

    public Account transferMoney(long fromAccountId, long toAccountId, BigDecimal amount) {
        validate(fromAccountId, toAccountId, amount);
        Account account = withdrawal(fromAccountId, amount);
        deposit(toAccountId, amount);
        return account;
    }

    public synchronized Account withdrawal(long id, BigDecimal amount) {
        Account account = getAccountById(id);
        BigDecimal accountBalance = account.getBalance();
        if (accountBalance == null || accountBalance.compareTo(amount) < 0) {
            throw new TransferException("Insufficient balance");
        }
        try {
            account.setBalance(account.getBalance().subtract(amount));
            Transaction transaction = createTransaction(amount.negate(), account, TRANSACTION_TYPE_DEBIT);
            account.addTransaction(transaction);
            accountRepository.save(account);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return account;
    }

    public Account getAccountById(long id) throws TransferException {
        Optional<Account> findById = accountRepository.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new TransferException("No account associated with this id");
        }
    }


    private void validate(long fromAccountId, long toAccountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferException("Invalid  amount");
        }
        if (fromAccountId > 0 && toAccountId > 0) {
            if (fromAccountId == toAccountId) {
                throw new TransferException("Transfer to same account not enabled");
            }
        }
        if (fromAccountId <= 0 || toAccountId <= 0) {
            throw new TransferException("Invalid account_id");
        }
    }
}