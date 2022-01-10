package com.zinkworks.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zinkworks.assessment.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
  BankAccount findByAccountNumberAndPin(Long accountNumber, Integer pin);
}
