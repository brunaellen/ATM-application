package com.zinkworks.assessment.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.model.BankAccountOperation;
import com.zinkworks.assessment.model.OperationType;
import com.zinkworks.assessment.repository.BankAccountOperationRepository;
import com.zinkworks.assessment.repository.BankAccountRepository;
import com.zinkworks.assessment.service.exception.AccountNotFoundException;
import com.zinkworks.assessment.service.exception.InsufficientBankAccountFundsException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;
import com.zinkworks.assessment.service.exception.InvalidPinException;

@Service
public class BankAccountService {
  
  @Autowired
  BankAccountRepository bankAccountRepository;
  
  @Autowired
  BankAccountOperationRepository accountOperationRepository;

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public BankAccount getBankAccount(Long accountNumber, Integer pinProvided) {
    final Optional<BankAccount> account = bankAccountRepository.findByAccountNumber(accountNumber);
    
    if (account.isEmpty()) {
      throw new AccountNotFoundException();
    } else {
      if (account.get().getPin().equals(pinProvided)) {
        return account.get();
      } else {
        throw new InvalidPinException();
      }
    }
  }

  public BigDecimal withdraw(BankAccount account, BigDecimal amount) {
    
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException("Amount should be greater than zero");
    }
           
    if(!accountHasEnoughFunds(amount, account)) {
      throw new InsufficientBankAccountFundsException();
    }
    
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
