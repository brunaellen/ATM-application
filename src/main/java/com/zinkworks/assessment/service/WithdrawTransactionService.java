package com.zinkworks.assessment.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zinkworks.assessment.model.BankAccount;

@Service
public class WithdrawTransactionService {
  
  @Autowired
  private BankAccountService bankAccountService;
  
  @Autowired
  private ATMService atmService;

  @Transactional
  public Map<Integer, Integer> withdraw(Long accountNumber, Integer pin, BigDecimal amount, Long id) {
    BankAccount account = bankAccountService.getBankAccount(accountNumber, pin);
    
    bankAccountService.withdraw(account, amount);
    Map<Integer, Integer> notesToBeDispensed = atmService.withdraw(amount, id);
    return notesToBeDispensed;
  }
}
