package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class BankAccountWithdrawResponse {
  private Map<Integer, Integer> notes;
  private LocalDateTime date = LocalDateTime.now();
  private Long accountNumber;
  private BigDecimal balance;
  
  public BankAccountWithdrawResponse(Long accountNumber, Map<Integer, Integer> notes, BigDecimal balance) {
    this.notes = notes;
    this.accountNumber = accountNumber;
    this.balance = balance;
  }
  
  public BigDecimal getBalance() {
    return balance;
  }
  
  public Map<Integer, Integer> getNotes() {
    return notes;
  }
  
  public String getDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return date.format(formatter);
  }
  
  public Long getAccountNumber() {
    return accountNumber;
  }
}
