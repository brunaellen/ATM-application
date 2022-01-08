package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.model.BankAccountOperation;

public class BankAccountResponse {

  private Long accountNumber;
  private Date date;
  private BigDecimal balance;
  private BigDecimal availableToSpend;
  private List<BankAccountOperation> statements;
  
  public BankAccountResponse(BankAccount account) {
    this.accountNumber = account.getAccountNumber();
    this.date = new Date();
    this.balance = account.getCurrentBalance();
    this.availableToSpend = account.getTotalFundsAvailable();
    this.statements = account.getOperations();
  }
  
  public Long getAccountNumber() {
    return accountNumber;
  }

  public Date getDate() {
    return date;
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
