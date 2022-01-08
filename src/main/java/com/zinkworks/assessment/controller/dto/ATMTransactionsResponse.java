package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.zinkworks.assessment.model.ATMOperation;
import com.zinkworks.assessment.model.Atm;

public class ATMTransactionsResponse {
  private List<ATMOperation> statements;
  private Map<Integer, Integer> notesAvailable;
  private Date date;
  private BigDecimal balance;
  
  public ATMTransactionsResponse(Atm atm) {
    this.statements = atm.getOperations();
    this.notesAvailable = atm.getNotesAvailable();
    this.date = new Date();
    this.balance = atm.getBalance();
  }

  public List<ATMOperation> getStatements() {
    return statements;
  }

  public Map<Integer, Integer> getNotesAvailable() {
    TreeMap<Integer, Integer> copy = new TreeMap<>(Comparator.reverseOrder());
    copy.putAll(Map.copyOf(notesAvailable));
    return copy;
  }

  public Date getDate() {
    return date;
  }

  public BigDecimal getBalance() {
    return balance;
  }
}
