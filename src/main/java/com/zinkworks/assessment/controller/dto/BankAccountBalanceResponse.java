package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;
import java.util.Date;

public class BankAccountBalanceResponse{
  private BigDecimal totalFundsForWithdraw;
  private BigDecimal balance;
  private Date date;
  
  public BankAccountBalanceResponse(BigDecimal balance, BigDecimal totalFundsForWithdraw) {
    this.balance = balance;
    this.totalFundsForWithdraw = totalFundsForWithdraw;
    this.date = new Date();
  }
  
  public BigDecimal getBalance() {
    return balance;
  }
  
  public BigDecimal getTotalFundsForWithdraw() {
    return totalFundsForWithdraw;
  }

  public Date getDate() {
    return date;
  }
}
