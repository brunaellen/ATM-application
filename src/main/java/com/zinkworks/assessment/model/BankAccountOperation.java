package com.zinkworks.assessment.model;

import java.math.BigDecimal;
import java.util.Date;

public class BankAccountOperation {
  private OperationType type;
  private BigDecimal amount;
  private Date date;

  public BankAccountOperation(OperationType type, BigDecimal amount) {
    this.type = type;
    this.amount = amount;
    this.date = new Date();
  }
  
  public BigDecimal getAmount() {
    return amount;
  }
  
  public OperationType getType() {
    return type;
  }
  
  public Date getDate() {
    return date;
  }
}
