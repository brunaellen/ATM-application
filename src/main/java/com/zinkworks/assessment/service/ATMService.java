package com.zinkworks.assessment.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zinkworks.assessment.model.ATMOperation;
import com.zinkworks.assessment.model.Atm;
import com.zinkworks.assessment.model.OperationType;
import com.zinkworks.assessment.repository.ATMOperationRepository;
import com.zinkworks.assessment.repository.AtmRepository;
import com.zinkworks.assessment.service.exception.InvalidAmountException;

@Service
public class ATMService {
  
  @Autowired
  private AtmRepository atmRepository;
  
  @Autowired
  private ATMOperationRepository atmOperationRepository;
  
  public boolean hasEnoughFunds(BigDecimal withdrawAmount, Long id) {
    Atm atm = atmRepository.findAllById(id);
    
    if(withdrawAmount.compareTo(atm.getBalance()) <= 0) {
      return true;
    } else {
      return false;
    }
  }

  public boolean canProcessWithdraw(BigDecimal withdrawAmount, Long id) {
    Atm atm = atmRepository.findAllById(id);
    
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
  
  public Map<Integer, Integer> withdraw(BigDecimal amount, Long id) {
    
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException("Amount should be greater than zero");
    }

    if(!hasEnoughFunds(amount, id)) {
      throw new InvalidAmountException("There is not enough money to process the withdraw.");
    }
    
    if(!canProcessWithdraw(amount, id)) {
      throw new InvalidAmountException("There is no combination of notes to process the withdraw.");
    }
    
    Atm atm = atmRepository.findAllById(id);
    
    final BigDecimal newBalance = atm.getBalance().subtract(amount);
    atmRepository.updateAtmBalance(newBalance, id);
    
    atmOperationRepository.save(new ATMOperation(OperationType.WITHDRAW, amount, atm));
    
    Map<Integer, Integer> notesToBeDispensed = getWithdrawSummaryNotes(amount, id);
    updateAtmNotesQuantity(notesToBeDispensed, id);

    return notesToBeDispensed;
  }
  
  private Map<Integer, Integer> updateAtmNotesQuantity(Map<Integer, Integer> notesToBeDispensed, Long id) {
    Atm atm = atmRepository.findAllById(id);
    Map<Integer, Integer> notesAvailableMap = atm.getNotesAvailable();
    
    notesToBeDispensed
      .entrySet()
      .stream()
      .forEach(entry -> {
        int olderValue = notesAvailableMap.get(entry.getKey());
        int newValue = olderValue - entry.getValue();
      
        if(newValue < olderValue && entry.getKey().equals(50)) {
          atmRepository.updateQuantityOfFifthNotes(id, newValue);
        }
        
        if(newValue < olderValue && entry.getKey().equals(20)) {
          atmRepository.updateQuantityOfTwentyNotes(id, newValue);
        }
        
        if(newValue < olderValue && entry.getKey().equals(10)) {
          atmRepository.updateQuantityOfTenNotes(id, newValue);
        }
        
        if(newValue < olderValue && entry.getKey().equals(5)) {
          atmRepository.updateQuantityOfFiveNotes(id, newValue);
        }
      });
    return notesAvailableMap;
  }

  private Map<Integer, Integer> getWithdrawSummaryNotes(BigDecimal withdrawAmount, Long id) {
    Atm atm = atmRepository.findAllById(id);
    
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
  
  
  public Atm getAtm(Long id) {
    return atmRepository.findAllById(id);
  }
}
