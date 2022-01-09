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
  private BigDecimal availableToSpend;
  private List<BankAccountOperation> statements;
  
  public BankAccountResponse(BankAccount account) {
    this.accountNumber = account.getAccountNumber();
    this.balance = account.getCurrentBalance();
    this.availableToSpend = account.getTotalFundsAvailable();
    this.statements = account.getOperations();
  }
  
  public Long getAccountNumber() {
    return accountNumber;
  }
  
  public String getDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return date.format(formatter);
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public BigDecimal getAvailableToSpend() {
    return availableToSpend;
  }

  public List<BankAccountOperation> getStatements() {
    return statements;
  }

}
