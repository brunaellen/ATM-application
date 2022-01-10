package com.zinkworks.assessment.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.service.exception.AccountNotFoundException;
import com.zinkworks.assessment.service.exception.InvalidPinException;

@Service
public class BankAccountService {

  public BankAccount getBankAccount(Long accountNumber, Integer pin) {
    BankAccount firstAccount = new BankAccount(123456789L, 1234, BigDecimal.valueOf(800), BigDecimal.valueOf(200));
    BankAccount secondAccount = new BankAccount(987654321L, 4321, BigDecimal.valueOf(1230), BigDecimal.valueOf(150));
    List<BankAccount> accounts = Arrays.asList(firstAccount, secondAccount);
    
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
  
  public void withdraw(BankAccount account, BigDecimal amount) {
    account.updateAvailableFunds(amount);
  }
}
