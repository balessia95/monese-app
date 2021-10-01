package com.monese.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monese.web.model.Account;
import com.monese.web.model.Transaction;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long>{

}
