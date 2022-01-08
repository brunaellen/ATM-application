package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class BankAccountWithdrawResponse {
  private Map<Integer, Integer> notes;
  private Date date;
  private Long accountNumber;
  private BigDecimal balance;
  
  public BankAccountWithdrawResponse(Long accountNumber, Map<Integer, Integer> notes, BigDecimal balance) {
    this.notes = notes;
    this.accountNumber = accountNumber;
    this.date = new Date();
    this.balance = balance;
  }
  
  public BigDecimal getBalance() {
    return balance;
  }
  
  public Map<Integer, Integer> getNotes() {
    return notes;
  }
  
  public Date getDate() {
    return date;
  }
  
  public Long getAccountNumber() {
    return accountNumber;
  }
}
