package com.monese.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monese.web.model.Account;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long>{

}
