package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.model.BankAccountOperation;

public class BankAccountResponse {

  private Long accountNumber;
  private LocalDateTime date = LocalDateTime.now();
  private BigDecimal balance;
  private BigDecimal overdraft;
  private BigDecimal totalFundsAvailable;
  private List<BankAccountOperation> statements;
  
  public BankAccountResponse(BankAccount account, BigDecimal totalFundsAvailable) {
    this.accountNumber = account.getAccountNumber();
    this.balance = account.getBalance();
    this.overdraft = account.getOverdraft();
    this.totalFundsAvailable = totalFundsAvailable;
    this.statements = account.getOperations();
  }
  
  public Long getAccountNumber() {
    return accountNumber;
  }
  
  public String getDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    return date.format(formatter);
  }

  public BigDecimal getBalance() {
    return balance;
  }
  
  public BigDecimal getOverdraft() {
    return overdraft;
  }

  public BigDecimal getTotalFundsAvailable() {
    return totalFundsAvailable;
  }

  public List<BankAccountOperation> getStatements() {
    return statements;
  }
}
