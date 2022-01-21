package com.zinkworks.assessment.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BankAccount.class})
class BankAccountTest {

  private BankAccount account = new BankAccount(123456789L, 1234, BigDecimal.valueOf(800), BigDecimal.valueOf(200));
  
  @Test
  void getAccountNumber_ShouldReturnAccountNumber() {
    assertThat(account.getAccountNumber()).isEqualTo(123456789L);
  }
  
  @Test
  void getPin_ShouldReturnPin() {
    assertThat(account.getPin()).isEqualTo(1234);
  }
  
  @Test
  void getBalance_ShouldReturnBalance() {
    assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(800D));
  }
  
  @Test
  void getOverdraft_ShouldReturnOverdraft() {
    assertThat(account.getOverdraft()).isEqualByComparingTo(BigDecimal.valueOf(200D));
  }
  
  @Test
  void getOperations_ShouldReturnEmptyIfNoOperationsExecuted() {
    assertThat(account.getOperations()).isEmpty();
  }
}
