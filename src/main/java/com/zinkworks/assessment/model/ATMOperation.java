package com.zinkworks.assessment.model;

import java.math.BigDecimal;
import java.util.Date;

public class ATMOperation {
  
  private OperationType type;
  private BigDecimal amount;
  private Date date;

  public ATMOperation(OperationType type, BigDecimal amount) {
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
