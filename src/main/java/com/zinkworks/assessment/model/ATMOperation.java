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
import javax.persistence.Table;

@Entity
@Table(name = "ATM_Operation")
public class ATMOperation {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal amount;
  private LocalDateTime date = LocalDateTime.now();
  
  @ManyToOne
  private Atm atm;
  
  @Enumerated(EnumType.STRING)
  private OperationType type;
  
  public ATMOperation() {
  }

  public ATMOperation(OperationType type, BigDecimal amount,Atm atm) {
    this.type = type;
    this.amount = amount;
    this.atm = atm;
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

  public Long getId() {
    return id;
  }
}
