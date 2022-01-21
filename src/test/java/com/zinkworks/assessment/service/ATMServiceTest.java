package com.zinkworks.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.zinkworks.assessment.model.Atm;
import com.zinkworks.assessment.repository.ATMOperationRepository;
import com.zinkworks.assessment.repository.AtmRepository;
import com.zinkworks.assessment.service.exception.AtmNotFoundException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;

@SpringBootTest(classes = {ATMService.class})
class ATMServiceTest {
  
  @Autowired
  private ATMService service;
  
  @MockBean
  private AtmRepository atmRepository;
  
  @MockBean
  private ATMOperationRepository atmOperationRepository;
  
  @Test
  void hasEnoughFunds_givenAmountAndAtm_shouldReturnTrueIfAmountIsLessThanOrEqualToATMFunds() {
    
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    
    assertThat(service.hasEnoughFunds(BigDecimal.valueOf(100), atm)).isTrue();
    assertThat(service.hasEnoughFunds(atm.getBalance(), atm)).isTrue();  
  }
  
  @Test
  void hasEnoughFunds_givenAnAmountAndAtm_shouldReturnFalseIfAmountIsGreaterThanATMFunds() {
    
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    
    assertThat(service.hasEnoughFunds(BigDecimal.valueOf(1600), atm)).isFalse();
  }
  
  @Test
  void canProcessWithdraw_givenAmountAndAtm_ifThereAreNotesToMatchAmount_shouldReturnTrue() {
    
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    BigDecimal withdrawAmount = BigDecimal.valueOf(1300);
    assertThat(service.canProcessWithdraw(withdrawAmount, atm)).isTrue();
  }
  
  @Test
  void canProcessWithdraw_givenAmountAndAtm_ifThereAreNoNotesToMatchAmount_shouldReturnFalse() {
    
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    
    assertThat(service.canProcessWithdraw(BigDecimal.valueOf(3), atm)).isFalse();

    assertThat(service.canProcessWithdraw(BigDecimal.valueOf(50.2), atm)).isFalse();
  }
  
  @Test
  void withdraw_ifAmountLessThanOrEqualsToZero_shouldThrowException() {
    
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    
    assertThatThrownBy( () -> service.withdraw(BigDecimal.valueOf(-1), atm))
      .isInstanceOf(InvalidAmountException.class);
    
    assertThatThrownBy( () -> service.withdraw(BigDecimal.ZERO, atm))
    .isInstanceOf(InvalidAmountException.class);
  }
  
  @Test
  void withdraw_ifAtmHasNotEnoughBalance_shouldThrowException() {
    
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    
    assertThatThrownBy( () -> service.withdraw(BigDecimal.valueOf(1600), atm))
      .isInstanceOf(InvalidAmountException.class);
  }
  
  @Test
  void withdraw_ifAtmCannotProcessWithdraw_shouldThrowException() {
    
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    
    assertThatThrownBy( () -> service.withdraw(BigDecimal.valueOf(3), atm))
      .isInstanceOf(InvalidAmountException.class);
  }
  
  @Test
  void withdraw_ifAtmHasEnoughBalanceAndCanProcessWithdraw_shouldReturnNotesToBeDispensed() {
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    
    Map<Integer, Integer> notesToBeDispensed = service.withdraw(BigDecimal.valueOf(1000), atm);
    Map<Integer, Integer> expectedNotes = new HashMap<>();
    expectedNotes.put(50, 10);
    expectedNotes.put(20, 25);
    expectedNotes.put(10, 0);
    expectedNotes.put(5, 0);
    
    assertThat(notesToBeDispensed).isEqualTo(expectedNotes);
  }
  
  @Test
  void withdraw_ifAtmHasEnoughBalanceAndCanProcessWithdraw_shouldUpdateAtmBalance() {
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    service.withdraw(BigDecimal.valueOf(50), atm);
    
    verify(atmRepository).updateAtmBalance(BigDecimal.valueOf(1450), atm.getId());
  }
  
  @Test
  void updateAtmNotesQuantity_givenNotesToBeDispensedAndAtm_shouldReturnNotesquantityUpdated() {
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    
    Map<Integer, Integer> notesToBeDispensed = new TreeMap<>(Comparator.reverseOrder());
    notesToBeDispensed.put(50, 2);
    notesToBeDispensed.put(20, 0);
    notesToBeDispensed.put(10, 0);
    notesToBeDispensed.put(5, 0);
    
    Map<Integer, Integer> expectedNotesUpdated = new TreeMap<>(Comparator.reverseOrder());
    expectedNotesUpdated.put(50, 8);
    expectedNotesUpdated.put(20, 30);
    expectedNotesUpdated.put(10, 30);
    expectedNotesUpdated.put(5, 20);
    
    assertThat(service.updateAtmNotesQuantity(notesToBeDispensed, atm))
      .isEqualTo(expectedNotesUpdated);
  }
 
  @Test
  void getWithdrawSummaryNotes_givenAmountAndAtm_shouldReturnSummaryOfNotesToBeDispensed() {
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    
    Map<Integer, Integer> expectedSummaryOfNotes = Map.of(50, 2, 20, 0, 10, 0, 5, 0);
    
    assertThat(service.getWithdrawSummaryNotes(BigDecimal.valueOf(100), atm))
      .isEqualTo(expectedSummaryOfNotes);
  }
  
  @Test
  void getAtm_givenAnId_shouldReturnAnAtm() {
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    when(atmRepository.findAllById(1L)).thenReturn(Optional.of(atm));
    
    assertThat(service.getAtm(1L)).isEqualTo(atm);
  }
  
  @Test
  void getAtm_givenAnId_shouldThrowExceptionIfAtmNotFound() {
    Atm atm = new Atm(1L, BigDecimal.valueOf(1500), 10, 30, 30, 20);
    when(atmRepository.findAllById(1L)).thenReturn(Optional.of(atm));

    assertThatThrownBy( () -> service.getAtm(2L)).isInstanceOf(AtmNotFoundException.class);
  }

}
