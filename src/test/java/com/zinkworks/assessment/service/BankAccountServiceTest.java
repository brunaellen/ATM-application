package com.zinkworks.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.service.exception.AccountNotFoundException;
import com.zinkworks.assessment.service.exception.InsufficientBankAccountFundsException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;
import com.zinkworks.assessment.service.exception.InvalidPinException;

class BankAccountServiceTest {

  private BankAccountService service = new BankAccountService();

  @Test
  void withdraw_ifBankAccountHasNotEnoughBalance_shouldThrowExceptionAndNotChangeBalance() {
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(1000), BigDecimal.valueOf(100));
    
    assertThatThrownBy( () -> service.withdraw(account , BigDecimal.valueOf(2000)))
      .isInstanceOf(InsufficientBankAccountFundsException.class);
    
    assertThat(account.getCurrentBalance()).isEqualTo(BigDecimal.valueOf(1000));
  }
  
  @Test
  void withdraw_ifBankAccountHasEnoughBalance_shouldWithdrawAmount() {
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(1000), BigDecimal.valueOf(100));
    
    service.withdraw(account , BigDecimal.valueOf(1000));
    
    assertThat(account.getCurrentBalance()).isEqualTo(BigDecimal.valueOf(0));
    assertThat(account.getOverdraft()).isEqualTo(BigDecimal.valueOf(100));
  }
  
  @Test
  void withdraw_ifBankAccountHasNotEnoughBalance_shouldCheckOverdraft() {
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(1000), BigDecimal.valueOf(100));
    
    service.withdraw(account , BigDecimal.valueOf(1100));
    
    assertThat(account.getCurrentBalance()).isEqualTo(BigDecimal.valueOf(0));
    assertThat(account.getOverdraft()).isEqualTo(BigDecimal.valueOf(0));
  }
  
  @Test
  void withdraw_shouldOnlyAcceptAmountGreaterThanZero() {
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(1000), BigDecimal.valueOf(100));
    
    assertThatThrownBy( () -> service.withdraw(account , BigDecimal.valueOf(-1))).isInstanceOf(InvalidAmountException.class);
    
    assertThat(account.getCurrentBalance()).isEqualTo(BigDecimal.valueOf(1000));
    assertThat(account.getOverdraft()).isEqualTo(BigDecimal.valueOf(100));
    
    assertThatThrownBy( () -> service.withdraw(account , BigDecimal.valueOf(0))).isInstanceOf(InvalidAmountException.class);
    
    assertThat(account.getCurrentBalance()).isEqualTo(BigDecimal.valueOf(1000));
    assertThat(account.getOverdraft()).isEqualTo(BigDecimal.valueOf(100));
  }
  
  @Test
  void getBankAccount_givenAccountNumberAndPinMatch_shouldReturnAccount() {
    BankAccount account = service.getBankAccount(123456789L, 1234);
    assertThat(account).isNotNull();
    assertThat(account.getAccountNumber()).isEqualTo(123456789L);
  }
  
  @Test
  void getBankAccount_shouldThrowExceptionIfPinDoesntMatch() {
    assertThatThrownBy( () -> service.getBankAccount(123456789L, 1111))
      .isInstanceOf(InvalidPinException.class);
  }
  
  @Test
  void getBankAccount_givenAccountDoesNotExist_shouldThrowException() {
    assertThatThrownBy( () -> service.getBankAccount(123L, 1111))
      .isInstanceOf(AccountNotFoundException.class);
  }
}
