package com.zinkworks.assessment.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.service.exception.InsufficientBankAccountFundsException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;

@Service
public class WithdrawTransactionService {
  
  @Autowired
  private BankAccountService bankAccountService;
  
  @Autowired
  private ATMService atmService;

  public Map<Integer, Integer> withdraw(Long accountNumber, Integer pin, BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException("Amount should be greater than zero");
    }
    
    BankAccount account = bankAccountService.getBankAccount(accountNumber, pin);
    if(!account.hasEnoughFunds(amount)) {
      throw new InsufficientBankAccountFundsException();
    }
    
    if(!atmService.hasEnoughFunds(amount)) {
      throw new InvalidAmountException("There is not enough money to process the withdraw.");
    }
    
    if(!atmService.canProcessWithdraw(amount)) {
      throw new InvalidAmountException("There is no combination of notes to process the withdraw.");
    }
    
    bankAccountService.withdraw(account, amount);
    return atmService.withdraw(amount);
  }
}
