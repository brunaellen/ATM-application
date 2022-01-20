package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zinkworks.assessment.model.BankAccount;

public class BankAccountWithdrawResponse {
  private List<Notes> notes;
  private LocalDateTime date = LocalDateTime.now();
  private Long accountNumber;
  private BigDecimal balance;
  private BigDecimal overdraft;

  public BankAccountWithdrawResponse(BankAccount account) {
    this.accountNumber = account.getAccountNumber();
    this.balance = account.getBalance();
    this.overdraft = account.getOverdraft();
  }

  public void setNotes(Map<Integer, Integer> notes) {
    List<Notes> notesList = new ArrayList<>();
    
    notes.entrySet().forEach(note -> {
      Integer currentFaceNote = note.getKey();
      Integer currentQuantity = note.getValue();
      notesList.add(new Notes(currentFaceNote, currentQuantity));
    });
    
    this.notes = notesList;
  }

  public BigDecimal getBalance() {
    return balance;
  }
  public BigDecimal getOverdraft() {
    return overdraft;
  }
  
  public List<Notes> getNotes() {
    return notes;
  }
  
  public String getDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return date.format(formatter);
  }
  
  public Long getAccountNumber() {
    return accountNumber;
  }
}
