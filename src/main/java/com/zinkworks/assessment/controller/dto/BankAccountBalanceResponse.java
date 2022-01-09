package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankAccountBalanceResponse{
  private BigDecimal totalFundsForWithdraw;
  private BigDecimal balance;
  private LocalDateTime date = LocalDateTime.now();
  
  public BankAccountBalanceResponse(BigDecimal balance, BigDecimal totalFundsForWithdraw) {
    this.balance = balance;
    this.totalFundsForWithdraw = totalFundsForWithdraw;
  }
  
  public BigDecimal getBalance() {
    return balance;
  }
  
  public BigDecimal getTotalFundsForWithdraw() {
    return totalFundsForWithdraw;
  }

  public String getDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return date.format(formatter);
  }
}
