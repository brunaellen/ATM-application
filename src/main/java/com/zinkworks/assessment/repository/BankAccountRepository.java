package com.zinkworks.assessment.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zinkworks.assessment.model.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
  Optional<BankAccount> findByAccountNumber(Long accountNumber);
  
  @Modifying(clearAutomatically = true)
  @Query("update BankAccount b set b.balance = ?1 where b.accountNumber = ?2")
  void updateBalance(BigDecimal newBalance, Long accountNumber);
  
  @Modifying(clearAutomatically = true)
  @Query("update BankAccount b set b.overdraft = ?1 where b.accountNumber = ?2")
  void updateOverdraft(BigDecimal newOverdraft, Long accountNumber);
}
