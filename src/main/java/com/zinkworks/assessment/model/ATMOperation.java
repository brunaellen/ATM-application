package com.zinkworks.assessment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ATMOperation {
  
  private OperationType type;
  private BigDecimal amount;
  private LocalDateTime date = LocalDateTime.now();

  public ATMOperation(OperationType type, BigDecimal amount) {
    this.type = type;
    this.amount = amount;
  }
  
  public BigDecimal getAmount() {
    return amount;
  }
  
  public OperationType getType() {
    return type;
  }
  
  public String getDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return date.format(formatter);
  }
}
