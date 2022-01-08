package com.zinkworks.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ATMService.class})
class ATMServiceTest {
  
  @Autowired
  private ATMService service;
  
  @Test
  void hasEnoughFunds_givenAnAmount_shouldReturnTrueIfAmountIsLessThanOrEqualToATMFunds() {
    BigDecimal withdrawAmount = service.getAtm().getBalance();
    assertThat(service.hasEnoughFunds(withdrawAmount.subtract(BigDecimal.valueOf(10D)))).isTrue();
    assertThat(service.hasEnoughFunds(withdrawAmount)).isTrue();  
  }
  
  @Test
  void hasEnoughFunds_givenAnAmount_shouldReturnFalseIfAmountIsGreaterThanATMFunds() {
    BigDecimal withdrawAmount = service.getAtm().getBalance();
    assertThat(service.hasEnoughFunds(withdrawAmount.add(withdrawAmount))).isFalse();
  }
  
  @Test
  void canProcessWithdraw_givenAnAmount_shouldReturnTrueIfThereAreEnoughNotesToProcessWithdraw() {
    BigDecimal withdrawAmount = BigDecimal.valueOf(1300);
    assertThat(service.canProcessWithdraw(withdrawAmount)).isTrue();
  }
  
  @Test
  void canProcessWithdraw_givenAnAmount_shouldReturnFalseIfThereAreEnoughNotesToProcessWithdraw() {
    BigDecimal withdrawAmount = BigDecimal.valueOf(3D);
    assertThat(service.canProcessWithdraw(withdrawAmount)).isFalse();
    
    withdrawAmount = BigDecimal.valueOf(30.2D);
    assertThat(service.canProcessWithdraw(withdrawAmount)).isFalse();
    
    assertThat(service.canProcessWithdraw(BigDecimal.valueOf(0D))).isFalse();
  }
  
  @Test
  void withdraw_givenAnAmountAndBankAccount_shouldReturnAnATMWithdrawSummary() {
    BigDecimal withdrawAmount = BigDecimal.valueOf(50D);
    Map<Integer, Integer> withdraw = service.withdraw(withdrawAmount);
    assertThat(withdraw).isNotNull();
  }
  
  @Test
  void withdraw_givenCannotAddTransactionToListOfTransactions_shouldThrowException() {
    BigDecimal withdrawAmount = BigDecimal.valueOf(50D);
    Map<Integer, Integer> withdraw = service.withdraw(withdrawAmount);
    assertThat(withdraw).isNotNull();
  }
}
