package com.zinkworks.assessment.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BankAccount.class})
class BankAccountTest {

  private BankAccount account = new BankAccount(123456789L, 1234, BigDecimal.valueOf(800), BigDecimal.valueOf(200));
  
  @Test
  void isPinValid_givenACorrectPinShouldReturnTrue() {
    assertThat(account.isPinValid(1234)).isTrue();
  }
}
