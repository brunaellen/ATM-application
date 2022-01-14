package com.zinkworks.assessment.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.service.exception.InsufficientBankAccountFundsException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;

@Service
public class WithdrawTransactionService {
  
  @Autowired
  private BankAccountService bankAccountService;
  
  @Autowired
  private ATMService atmService;

  @Transactional
  public Map<Integer, Integer> withdraw(Long accountNumber, Integer pin, BigDecimal amount, Long id) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException("Amount should be greater than zero");
    }
    
    BankAccount account = bankAccountService.getBankAccount(accountNumber, pin);
           
    if(!bankAccountService.accountHasEnoughFunds(amount, account)) {
      throw new InsufficientBankAccountFundsException();
    }
    
    if(!atmService.hasEnoughFunds(amount, id)) {
      throw new InvalidAmountException("There is not enough money to process the withdraw.");
    }
    
    if(!atmService.canProcessWithdraw(amount, id)) {
      throw new InvalidAmountException("There is no combination of notes to process the withdraw.");
    }
  
    bankAccountService.withdraw(account, amount);
    Map<Integer, Integer> notesToBeDispensed = atmService.withdraw(amount, id);
    return notesToBeDispensed;
  }
}
