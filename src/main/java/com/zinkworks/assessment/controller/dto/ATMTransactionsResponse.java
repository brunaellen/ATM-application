package com.zinkworks.assessment.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zinkworks.assessment.model.ATMOperation;
import com.zinkworks.assessment.model.Atm;

public class ATMTransactionsResponse {
  private List<ATMOperation> statements;
  private List<Notes> notesAvailable;
  private LocalDateTime date = LocalDateTime.now();
  private BigDecimal balance;
  
  public ATMTransactionsResponse(Atm atm) {
    this.statements = atm.getOperations();
    this.balance = atm.getBalance();
  }
  
  public void setStatements(Map<Integer, Integer> statements) {
    List<Notes> notesList = new ArrayList<>();
    
    statements.entrySet().forEach(note -> {
      Integer currentFaceNote = note.getKey();
      Integer currentQuantity = note.getValue();
      notesList.add(new Notes(currentFaceNote, currentQuantity));
    });
    
    this.notesAvailable = notesList;
  }
  
  public List<Notes> getNotesAvailable() {
    return notesAvailable;
  }

  public List<ATMOperation> getStatements() {
    return statements;
  }

  public String getDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return date.format(formatter);
  }

  public BigDecimal getBalance() {
    return balance;
  }
}
