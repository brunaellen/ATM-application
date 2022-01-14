package com.zinkworks.assessment.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.model.BankAccountOperation;
import com.zinkworks.assessment.model.OperationType;
import com.zinkworks.assessment.repository.BankAccountOperationRepository;
import com.zinkworks.assessment.repository.BankAccountRepository;
import com.zinkworks.assessment.service.exception.AccountNotFoundException;
import com.zinkworks.assessment.service.exception.InvalidPinException;

@Service
public class BankAccountService {
  
  @Autowired
  BankAccountRepository bankAccountRepository;
  
  @Autowired
  BankAccountOperationRepository accountOperationRepository;

  public BankAccount getBankAccount(Long accountNumber, Integer pin) {
    List<BankAccount> accounts = bankAccountRepository.findByAccountNumberAndPin(accountNumber, pin);
    
    final Optional<BankAccount> account = accounts
      .stream()
      .filter(bankAccount -> bankAccount.getAccountNumber().equals(accountNumber))
      .findAny();
    
    if (account.isEmpty()) {
      throw new AccountNotFoundException();
    } else {
      if (account.get().isPinValid(pin)) {
        return account.get();
      } else {
        throw new InvalidPinException();
      }
    }
  }

  public BigDecimal withdraw(BankAccount account, BigDecimal amount) {
    BigDecimal newBalance = account.getBalance().subtract(amount);
    accountOperationRepository.save(new BankAccountOperation(OperationType.WITHDRAW, amount, account));
      
    Long accountNumber = account.getAccountNumber();
 
    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
      BigDecimal newOverdraft = account.getOverdraft().add(newBalance);
      newBalance = BigDecimal.ZERO;

      bankAccountRepository.updateBalance(newBalance, accountNumber);
      bankAccountRepository.updateOverdraft(newOverdraft, accountNumber);
    } else {
      bankAccountRepository.updateBalance(newBalance, accountNumber);
    }
    return account.getBalance();
  }
  
  public boolean accountHasEnoughFunds(BigDecimal amount, BankAccount account) {
    if(amount.compareTo(getTotalFundsAvailable(account)) <= 0) {
      return true;
    } else {
      return false;
    }
  }
  
  public BigDecimal getTotalFundsAvailable(BankAccount account) {
    return account.getTotalFundsAvailable();
  }
}
