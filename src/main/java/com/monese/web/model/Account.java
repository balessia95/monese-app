package com.monese.web.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "account")
public class Account {
	@Id
	@Column(name = "ACCOUNT_ID")
	private Long id;
	private BigDecimal balance;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
	@JsonManagedReference
	private List<Transaction> transactions;

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
		transaction.setAccount(this);
	}
}