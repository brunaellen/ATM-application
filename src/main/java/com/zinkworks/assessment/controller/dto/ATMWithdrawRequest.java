package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;

public class ATMWithdrawRequest {
  
  private Long accountNumber;
  private Integer pin;
  private BigDecimal amount;
  
  public ATMWithdrawRequest(Long accountNumber, Integer pin, BigDecimal amount) {
    this.accountNumber = accountNumber;
    this.pin = pin;
    this.amount = amount;
  }
  
  public Long getAccountNumber() {
    return accountNumber;
  }
  
  public BigDecimal getAmount() {
    return amount;
  }
  
  public Integer getPin() {
    return pin;
  }

}
