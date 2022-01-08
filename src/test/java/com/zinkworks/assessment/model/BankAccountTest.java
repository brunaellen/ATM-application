package com.zinkworks.assessment.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.zinkworks.assessment.service.exception.InsufficientBankAccountFundsException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;

@SpringBootTest(classes = {BankAccount.class})
class BankAccountTest {

  private BankAccount account = new BankAccount(123456789L, 1234, BigDecimal.valueOf(800), BigDecimal.valueOf(200));
  
  @Test
  void isPinValid_givenACorrectPinShouldReturnTrue() {
    assertThat(account.isPinValid(1234)).isTrue();
  }

  @Test
  void updateAvailableFunds_givenAnAmount_shouldThrowExceptionIfAmountIsLessThanOrEqualsToZero() {
    assertThatThrownBy(() -> account.updateAvailableFunds(BigDecimal.valueOf(0))).isInstanceOf(InvalidAmountException.class);
    assertThatThrownBy(() -> account.updateAvailableFunds(BigDecimal.valueOf(-1))).isInstanceOf(InvalidAmountException.class);
  }
  
  @Test
  void updateAvailableFunds_givenAnAmount_shouldThrowExceptionIfInsuficientFunds() {
    assertThatThrownBy(() -> account.updateAvailableFunds(BigDecimal.valueOf(100000)))
      .isInstanceOf(InsufficientBankAccountFundsException.class);
  }
  
  @Test
  void updateAvailableFunds_givenAnAmount_shouldUpdateFunds() {
    account.updateAvailableFunds(BigDecimal.valueOf(100));
    assertThat(account.getCurrentBalance()).isEqualTo(BigDecimal.valueOf(700));
    assertThat(account.getOverdraft()).isEqualTo(BigDecimal.valueOf(200));
    assertThat(account.getTotalFundsAvailable()).isEqualTo(BigDecimal.valueOf(900));
  }
  
  @Test
  void hasEnoughFunds_givenAnAmount_shouldReturnTrueIfTotalFundsAvailable() {
    assertThat(account.hasEnoughFunds(BigDecimal.valueOf(850))).isTrue();
    assertThat(account.getTotalFundsAvailable()).isEqualTo(BigDecimal.valueOf(1000));
  }
}
