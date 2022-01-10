package com.zinkworks.assessment.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BankAccountOperation {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal amount;
  private Date date;
  
  @Enumerated(EnumType.STRING)
  private OperationType type;

  @ManyToOne
  private BankAccount bankAccount;
  

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
