package com.zinkworks.assessment.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {Atm.class})
class AtmTest {

  @Autowired
  private Atm atm;
  
  @Test
  void getBalance_shouldReturnAtmInitialValue() {
    assertThat(atm.getBalance()).isEqualTo(BigDecimal.valueOf(1500D));
  }

  @Test
  void getNotesAvailable_shouldReturnNotesAvailable() {
    Map<Integer, Integer> notesAvailable = new TreeMap<>(Comparator.reverseOrder());
    notesAvailable.put(5, 20);
    notesAvailable.put(10, 30);
    notesAvailable.put(20, 30);
    notesAvailable.put(50, 10);
    assertThat(atm.getNotesAvailable()).isEqualTo(notesAvailable);
  }
  
  @Test
  void subtract_givenAmountAndSummaryOfNotes_shouldReturnTrueIfWithdrawOccurred() {
    Map<Integer, Integer> summaryOfNotes = new TreeMap<>(Comparator.reverseOrder());
    summaryOfNotes.put(5, 0);
    summaryOfNotes.put(10, 0);
    summaryOfNotes.put(20, 0);
    summaryOfNotes.put(50, 0);

    assertThat(atm.subtract(BigDecimal.valueOf(50D), summaryOfNotes)).isTrue();
    BigDecimal withdrawOperation = atm.getOperations().get(0).getAmount();
    assertThat(withdrawOperation).isEqualTo(BigDecimal.valueOf(50D));
  }
}