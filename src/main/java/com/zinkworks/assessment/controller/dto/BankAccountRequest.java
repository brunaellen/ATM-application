package com.zinkworks.assessment.controller.dto;

public class BankAccountRequest {
  private Long accountNumber;
  private Integer pin;
  
  public BankAccountRequest(Long accountNumber, Integer pin) {
    this.accountNumber = accountNumber;
    this.pin = pin;
  }
  
  public Long getAccountNumber() {
    return accountNumber;
  }
  
  public Integer getPin() {
    return pin;
  }
}
