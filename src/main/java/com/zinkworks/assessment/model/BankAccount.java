package com.zinkworks.assessment.model;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.zinkworks.assessment.service.exception.InsufficientBankAccountFundsException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;
import com.zinkworks.assessment.service.exception.InvalidPinException;

@Entity
public class BankAccount {
  
  //public static BankAccount ACCOUNT_1 = new BankAccount(123456789L, 1234, BigDecimal.valueOf(800), BigDecimal.valueOf(200));
  //public static BankAccount ACCOUNT_2 = new BankAccount(987654321L, 4321, BigDecimal.valueOf(1230), BigDecimal.valueOf(150));
  
  @Id
  private Long accountNumber;
  private Integer pin;
  private BigDecimal balance;
  private BigDecimal overdraft;
  
  @OneToMany(mappedBy = "bankAccount")
  private List<BankAccountOperation> operations;
  
  public BankAccount(){
  }
  
  public BankAccount(Long accountNumber, Integer pin, BigDecimal balance, BigDecimal overdraft) {
    this.accountNumber = accountNumber;
    this.pin = pin;
    this.balance = balance;
    this.overdraft = overdraft;
    this.operations = new LinkedList<>();
  }
  
  public BigDecimal getTotalFundsAvailable() {
    return balance.add(overdraft);
  }

  public Long getAccountNumber() {
    return accountNumber;
  }

  public boolean isPinValid(Integer pinProvided) {
    if(pinProvided.equals(pin)) {
      return true;
    }
    throw new InvalidPinException();
  }
  
  public BigDecimal getCurrentBalance() {
    return balance;
  }
  
  public BigDecimal getOverdraft() {
    return overdraft;
  }
  
  public List<BankAccountOperation> getOperations() {
    return List.copyOf(operations);
  }
  
  public void updateAvailableFunds(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException("Amount should be greater than zero");
    }
    
    if (hasEnoughFunds(amount)) {
      this.operations.add(new BankAccountOperation(OperationType.WITHDRAW, amount));
      BigDecimal newBalance = balance.subtract(amount); 
      if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
        BigDecimal newOverdraft = overdraft.add(newBalance);
        newBalance = BigDecimal.ZERO;
        
        balance = newBalance;
        overdraft = newOverdraft;
      } else {
        balance = newBalance;
      }
    } else {
      throw new InsufficientBankAccountFundsException();
    }  
  }

  public boolean hasEnoughFunds(BigDecimal amount) {
    if(amount.compareTo(getTotalFundsAvailable()) <= 0) {
      return true;
    } else {
      return false;
    }
  }
}
