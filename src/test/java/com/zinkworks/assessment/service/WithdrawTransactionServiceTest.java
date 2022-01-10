package com.zinkworks.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.service.exception.AccountNotFoundException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;
import com.zinkworks.assessment.service.exception.InvalidPinException;

@SpringBootTest(classes = {WithdrawTransactionService.class})
class WithdrawTransactionServiceTest {

  @Autowired
  private WithdrawTransactionService service;
  
  @MockBean
  private BankAccountService bankAccountService;
  
  @MockBean
  private ATMService atmService;
  
  @Test
  void withdraw_givenAccountAndATMCanProcessWithdraw_shouldProcessWithdraw() {
    when(atmService.hasEnoughFunds(BigDecimal.valueOf(100), 1L)).thenReturn(true);
    when(atmService.canProcessWithdraw(BigDecimal.valueOf(100), 1L)).thenReturn(true);
    
    when(bankAccountService.getBankAccount(1L, 1))
      .thenReturn(new BankAccount(1L, 1, BigDecimal.valueOf(100), BigDecimal.valueOf(100)));
    
    Map<Integer, Integer> withdraw = service.withdraw(1L, 1, BigDecimal.valueOf(100), 1L);
    assertThat(withdraw).isNotNull();
  }
  
  @Test
  void withdraw_givenAccountHasNotEnoughMoney_shouldThrowException() {
    when(atmService.hasEnoughFunds(BigDecimal.valueOf(100), 1L)).thenReturn(false);
    when(atmService.canProcessWithdraw(BigDecimal.valueOf(100), 1L)).thenReturn(true);
    
    when(bankAccountService.getBankAccount(1L, 1))
      .thenReturn(new BankAccount(1L, 1, BigDecimal.valueOf(100), BigDecimal.valueOf(100)));
    
    assertThatThrownBy(() -> service.withdraw(1L, 1, BigDecimal.valueOf(100), 1L))
      .isInstanceOf(InvalidAmountException.class);
  }
  
  @Test
  void withdraw_givenAtmHasNotEnoughMoney_shouldThrowException() {
    when(atmService.hasEnoughFunds(BigDecimal.valueOf(100), 1L)).thenReturn(true);
    when(atmService.canProcessWithdraw(BigDecimal.valueOf(100), 1L)).thenReturn(false);
    
    when(bankAccountService.getBankAccount(1L, 1))
      .thenReturn(new BankAccount(1L, 1, BigDecimal.valueOf(100), BigDecimal.valueOf(100)));
    
    assertThatThrownBy(() -> service.withdraw(1L, 1, BigDecimal.valueOf(100), 1L))
      .isInstanceOf(InvalidAmountException.class);
  }
  
  @Test
  void withdraw_givenBankAccountDoesntMatchPin_shouldThrowException() {
    when(atmService.hasEnoughFunds(BigDecimal.valueOf(100), 1L)).thenReturn(true);
    when(atmService.canProcessWithdraw(BigDecimal.valueOf(100), 1L)).thenReturn(true);
    
    when(bankAccountService.getBankAccount(1L, 10))
      .thenThrow(InvalidPinException.class);
    
    assertThatThrownBy(() -> service.withdraw(1L, 10, BigDecimal.valueOf(100), 1L))
      .isInstanceOf(InvalidPinException.class);
  }

  @Test
  void withdraw_givenBankAccountDoesntExist_shouldThrowException() {
    when(bankAccountService.getBankAccount(1L, 10))
      .thenThrow(AccountNotFoundException.class);
    
    assertThatThrownBy(() -> service.withdraw(1L, 10, BigDecimal.valueOf(100), 1L))
      .isInstanceOf(AccountNotFoundException.class);
  }
  
  @Test
  void withdraw_givenAmountIsLessThanZero_shouldThrowException() {
    
    assertThatThrownBy(() -> service.withdraw(1L, 10, BigDecimal.valueOf(-100), 1L))
      .isInstanceOf(InvalidAmountException.class);
  }
}
