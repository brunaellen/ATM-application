package com.zinkworks.assessment.model;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class BankAccount {

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

  public Integer getPin() {
    return pin;
  }
  
  public BigDecimal getBalance() {
    return balance;
  }
  
  public BigDecimal getOverdraft() {
    return overdraft;
  }
  
  public List<BankAccountOperation> getOperations() {
    return List.copyOf(operations);
  }
}
