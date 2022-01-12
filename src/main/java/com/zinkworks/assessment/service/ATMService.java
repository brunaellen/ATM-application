package com.zinkworks.assessment.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zinkworks.assessment.model.Atm;
import com.zinkworks.assessment.service.exception.ErrorDuringTransactionException;

@Service
public class ATMService {
  private final Atm atm = new Atm();
  
  public boolean hasEnoughFunds(BigDecimal withdrawAmount) {
    if(withdrawAmount.compareTo(atm.getBalance()) <= 0) {
      return true;
    } else {
      return false;
    }
  }

  public boolean canProcessWithdraw(BigDecimal withdrawAmount) {
    
    if (withdrawAmount.compareTo(BigDecimal.ZERO) <= 0) {
      return false;
    }

    BigDecimal temporaryAmount = withdrawAmount;
    
    for (Map.Entry<Integer, Integer> notes : atm.getNotesAvailable().entrySet()) {
      Integer currentNoteValue = notes.getKey();
      Integer currentNoteCount = notes.getValue(); 
      Integer numberOfNotesNeeded = temporaryAmount.divide(BigDecimal.valueOf(currentNoteValue)).intValue();
      BigDecimal amountInCurrentNotes;
      if (numberOfNotesNeeded <= currentNoteCount) { 
        amountInCurrentNotes = BigDecimal.valueOf(numberOfNotesNeeded * currentNoteValue); 
      } else {
        amountInCurrentNotes = BigDecimal.valueOf(currentNoteValue * currentNoteCount);
      }
      
      temporaryAmount = temporaryAmount.subtract(amountInCurrentNotes); 
    }
    
    if (temporaryAmount.compareTo(BigDecimal.ZERO) > 0) {
      return false;
    } else {
      return true;
    }
  }
  
  public Map<Integer, Integer> withdraw(BigDecimal amount) {
    Map<Integer, Integer> notes = getWithdrawSummaryNotes(amount);
    if (atm.subtract(amount, notes)) {
      return notes;
    } else {
      throw new ErrorDuringTransactionException();
    }
  }

  public Map<Integer, Integer> getWithdrawSummaryNotes(BigDecimal withdrawAmount) {
    Map<Integer, Integer> summary = new HashMap<>();
    BigDecimal temporaryAmount = withdrawAmount;
    
    for (Map.Entry<Integer, Integer> notes : atm.getNotesAvailable().entrySet()) {
      Integer currentNoteValue = notes.getKey();
      Integer currentNoteCount = notes.getValue();
      BigDecimal numberOfNotesNeeded = temporaryAmount.divide(BigDecimal.valueOf(currentNoteValue));
      
      if (numberOfNotesNeeded.intValue() <= currentNoteCount) {
        summary.put(currentNoteValue, numberOfNotesNeeded.intValue());
        temporaryAmount = temporaryAmount.subtract(numberOfNotesNeeded.multiply(BigDecimal.valueOf(currentNoteValue)));
      } else {
        summary.put(currentNoteValue, currentNoteCount);
        temporaryAmount = temporaryAmount.subtract(BigDecimal.valueOf(currentNoteCount * currentNoteValue));
      }
    }
    return summary;
  }
  
  public Atm getAtm() {
    return this.atm;
  }
}
