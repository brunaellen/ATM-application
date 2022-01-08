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
  
  private final List<BankAccount> accounts = Arrays.asList(BankAccount.ACCOUNT_1, BankAccount.ACCOUNT_2);

  public BankAccount getBankAccount(Long accountNumber, Integer pin) {
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
