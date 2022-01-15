package com.zinkworks.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    
    Map<Integer, Integer> notesToBeDispensed = service.withdraw(BigDecimal.valueOf(50), atm);
    Map<Integer, Integer> expectedNotes = new HashMap<>();
    expectedNotes.put(50, 1);
    expectedNotes.put(20, 0);
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
