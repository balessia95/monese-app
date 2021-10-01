package com.monese.web.bank;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.monese.web.exception.TransferException;
import com.monese.web.model.Statement;
import com.monese.web.service.AccountService;
import com.monese.web.service.AccountStatementService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ApplicationTests {

	@Autowired
	AccountService accountService;
	@Autowired
	AccountStatementService accountStatementService;

	@Test(expected = TransferException.class)
	public void transferInvalidAmount() {
		accountService.transferMoney(11l, 22l, BigDecimal.valueOf(-10000));
	}

	@Test(expected = TransferException.class)
	public void transferInvalidAccount() {
		accountService.transferMoney(100l, 22l, BigDecimal.valueOf(10000));
	}

	@Test(expected = TransferException.class)
	public void transferInvalidBalance() {
		accountService.transferMoney(11l, 22l, BigDecimal.valueOf(200000));
	}

	@Test
	public void shouldGetStatement() {
		Statement statement = accountStatementService.getAccountStatement(11l);
		assertThat(statement.getAccountId()).isEqualTo(11l);
		assertThat(statement.getBalance()).isNotNull();
		assertThat(statement.getTransactions()).isNotNull();
	}

	@Test
	public void testTransferMoney(){
		Statement previousToAccountStatement = accountStatementService.getAccountStatement(11l);
		Statement previousFromAccountStatement = accountStatementService.getAccountStatement(22l);

		accountService.transferMoney(22l, 11l, BigDecimal.valueOf(3000));

		Statement statement = accountStatementService.getAccountStatement(11l);
		assertThat(statement.getBalance().intValue()).isEqualTo(previousToAccountStatement.getBalance().intValue() + 3000);
		assertThat(statement.getTransactions().size()).isEqualTo(1);

		Statement debitStatement = accountStatementService.getAccountStatement(22l);
		assertThat(debitStatement.getBalance().intValue()).isEqualTo(previousFromAccountStatement.getBalance().intValue() - 3000);
	}
}
