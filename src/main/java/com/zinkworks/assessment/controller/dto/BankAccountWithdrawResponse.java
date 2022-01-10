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
  private BigDecimal overdraft;
  
  public BankAccountWithdrawResponse(Long accountNumber, Map<Integer, Integer> notes, BigDecimal balance, BigDecimal overdraft) {
    this.notes = notes;
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.overdraft = overdraft;
  }
  
  public BigDecimal getBalance() {
    return balance;
  }
  public BigDecimal getOverdraft() {
    return overdraft;
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
