package com.zinkworks.assessment.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zinkworks.assessment.model.ATMOperation;
import com.zinkworks.assessment.model.Atm;
import com.zinkworks.assessment.model.OperationType;
import com.zinkworks.assessment.repository.ATMOperationRepository;
import com.zinkworks.assessment.repository.AtmRepository;
import com.zinkworks.assessment.service.exception.AtmNotFoundException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;

@Service
public class ATMService {
  
  @Autowired
  private AtmRepository atmRepository;
  
  @Autowired
  private ATMOperationRepository atmOperationRepository;
  
  public boolean hasEnoughFunds(BigDecimal withdrawAmount, Atm atm) {
    if(withdrawAmount.compareTo(atm.getBalance()) <= 0) {
      return true;
    } else {
      return false;
    }
  }

  public boolean canProcessWithdraw(BigDecimal withdrawAmount, Atm atm) {
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
  
  public Map<Integer, Integer> withdraw(BigDecimal amount, Atm atm) {
    
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException("Amount should be greater than zero");
    }

    if(!hasEnoughFunds(amount, atm)) {
      throw new InvalidAmountException("There is not enough money to process the withdraw.");
    }
    
    if(!canProcessWithdraw(amount, atm)) {
      throw new InvalidAmountException("There is no combination of notes to process the withdraw.");
    }

    final BigDecimal newBalance = atm.getBalance().subtract(amount);
    atmRepository.updateAtmBalance(newBalance, atm.getId());
    
    atmOperationRepository.save(new ATMOperation(OperationType.WITHDRAW, amount, atm));
    
    Map<Integer, Integer> notesToBeDispensed = getWithdrawSummaryNotes(amount, atm);
    updateAtmNotesQuantity(notesToBeDispensed, atm);

    return notesToBeDispensed;
  }
  
  protected Map<Integer, Integer> updateAtmNotesQuantity(Map<Integer, Integer> notesToBeDispensed, Atm atm) {
    Map<Integer, Integer> notesAvailable = atm.getNotesAvailable();
    Map<Integer, Integer> notesAvailableUpdated = new TreeMap<>(Comparator.reverseOrder());
    
    notesToBeDispensed
      .entrySet()
      .stream()
      .forEach(entry -> {
        int olderValue = notesAvailable.get(entry.getKey());
        int newValue = olderValue - entry.getValue();
        
        notesAvailableUpdated.put(entry.getKey(), newValue);

        if(entry.getKey().equals(50)) {
          atmRepository.updateQuantityOfFifthNotes(atm.getId(), newValue);
        }
        
        if(entry.getKey().equals(20)) {
          atmRepository.updateQuantityOfTwentyNotes(atm.getId(), newValue);
        }
        
        if(entry.getKey().equals(10)) {
          atmRepository.updateQuantityOfTenNotes(atm.getId(), newValue);
        }
        
        if(entry.getKey().equals(5)) {
          atmRepository.updateQuantityOfFiveNotes(atm.getId(), newValue);
        }    
      });
    
    return notesAvailableUpdated;
  }

  public Map<Integer, Integer> getWithdrawSummaryNotes(BigDecimal withdrawAmount, Atm atm) {
    Map<Integer, Integer> summary = new TreeMap<>(Comparator.reverseOrder());
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
    final Optional<Atm> atm = atmRepository.findAllById(id);
    
    if(atm.isEmpty()) {
      throw new AtmNotFoundException();
    }
    return atm.get();
  }
}
