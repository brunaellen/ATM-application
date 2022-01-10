package com.zinkworks.assessment.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Atm {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal balance;
  
  @ElementCollection
  private Map<Integer, Integer> notesAvailable;
  
  @OneToMany(mappedBy = "atm")
  private List<ATMOperation> operations;

  public Atm() {
    balance = BigDecimal.valueOf(1500D);
    operations = new LinkedList<>();
    notesAvailable = new TreeMap<>(Comparator.reverseOrder());
    notesAvailable.put(50, 10);
    notesAvailable.put(20, 30);
    notesAvailable.put(10, 30);
    notesAvailable.put(5, 20);
  }
  
  public BigDecimal getBalance() {
    return operations
        .stream()
        .map(operation -> operation.getAmount())
        .reduce(balance, BigDecimal::subtract);
  }
  
  public Map<Integer, Integer> getNotesAvailable() {
    TreeMap<Integer, Integer> copy = new TreeMap<>(Comparator.reverseOrder());
    copy.putAll(Map.copyOf(notesAvailable));
    return copy;
  }

  public boolean subtract(BigDecimal withdrawAmount, Map<Integer, Integer> summaryOfNotes) {
    ATMOperation atmOperation = new ATMOperation(OperationType.WITHDRAW, withdrawAmount);
    
    if (operations.add(atmOperation)) {
      summaryOfNotes
        .entrySet()
        .stream()
        .forEach(entry -> {
          int newValue = notesAvailable.get(entry.getKey()) - entry.getValue();
          notesAvailable.put(entry.getKey(), newValue); 
        });
      return true;
    } else {
      return false;
    }
  }

  public List<ATMOperation> getOperations() {
    return List.copyOf(operations);
  }
}
