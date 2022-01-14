package com.zinkworks.assessment.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
  
  private Integer fifthNotes;
  private Integer twentyNotes;
  private Integer tenNotes;
  private Integer fiveNotes;
  
  @OneToMany(mappedBy = "atm")
  private List<ATMOperation> operations;
  
  public Atm() {
  }

  public Atm(Long id, BigDecimal balance, Integer fifthNotes, Integer twentyNotes, Integer tenNotes, Integer fiveNotes) {
    this.id = id;
    this.balance = balance;
    this.fifthNotes = fifthNotes;
    this.twentyNotes = twentyNotes;
    this.tenNotes = tenNotes;
    this.fiveNotes = fiveNotes;
    this.operations = new ArrayList<>();
  }

  public Map<Integer, Integer> getNotesAvailable() {
    TreeMap<Integer, Integer> notesAvailableMap = new TreeMap<>(Comparator.reverseOrder());
    notesAvailableMap.put(50, fifthNotes);
    notesAvailableMap.put(20, twentyNotes);
    notesAvailableMap.put(10, tenNotes);
    notesAvailableMap.put(5, fiveNotes);
    return notesAvailableMap;
  }

  public List<ATMOperation> getOperations() {
    return List.copyOf(operations);
  }

  public BigDecimal getBalance() {
    return balance;
  }
}
