package com.zinkworks.assessment.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {Atm.class})
class AtmTest {

  private Atm atm = new Atm(1L, BigDecimal.valueOf(1500D), 10, 30, 30, 20);;
  
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
  void getOperations_shouldReturnEmptyOperations() {
    assertThat(atm.getOperations()).isEmpty();
  }
}
