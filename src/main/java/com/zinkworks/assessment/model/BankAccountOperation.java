package com.zinkworks.assessment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
  private LocalDateTime date = LocalDateTime.now();
  
  @Enumerated(EnumType.STRING)
  private OperationType type;

  @ManyToOne
  private BankAccount bankAccount;
  
  public BankAccountOperation() {
  }
  

  public BankAccountOperation(OperationType type, BigDecimal amount, BankAccount bankAccount) {
    this.type = type;
    this.amount = amount;
    this.bankAccount = bankAccount;
  }
  
  public BigDecimal getAmount() {
    return amount;
  }
  
  public OperationType getType() {
    return type;
  }
  
  public String getDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    return date.format(formatter);
  }
}
