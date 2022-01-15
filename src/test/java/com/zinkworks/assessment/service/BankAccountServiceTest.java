package com.zinkworks.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.repository.BankAccountOperationRepository;
import com.zinkworks.assessment.repository.BankAccountRepository;
import com.zinkworks.assessment.service.exception.AccountNotFoundException;
import com.zinkworks.assessment.service.exception.InsufficientBankAccountFundsException;
import com.zinkworks.assessment.service.exception.InvalidAmountException;
import com.zinkworks.assessment.service.exception.InvalidPinException;

@SpringBootTest(classes = {BankAccountService.class})
class BankAccountServiceTest {

  @MockBean
  private BankAccountRepository repository;
  
  @MockBean
  private BankAccountOperationRepository accountOperationRepository;
  
  @Autowired
  private BankAccountService service;

  @Test
  void getBankAccount_IfPinDoesNotMatch_shouldThrowException() {
    
    BankAccount bankAccount = new BankAccount(123456789L, 1234, BigDecimal.valueOf(200), BigDecimal.valueOf(100));
    Optional<BankAccount> accountFromRepository = Optional.of(bankAccount);
    when(repository.findByAccountNumber(123456789L)).thenReturn(accountFromRepository);
    
    assertThatThrownBy( () -> service.getBankAccount(123456789L, 1111))
      .isInstanceOf(InvalidPinException.class);
  }
  
  @Test
  void getBankAccount_ifAccountDoesNotExist_shouldThrowException() {
    
    BankAccount bankAccount = new BankAccount(123456789L, 1234, BigDecimal.valueOf(200), BigDecimal.valueOf(100));
    Optional<BankAccount> accountFromRepository = Optional.of(bankAccount);
    when(repository.findByAccountNumber(123456789L)).thenReturn(accountFromRepository);
    
    assertThatThrownBy( () -> service.getBankAccount(123L, 1234))
      .isInstanceOf(AccountNotFoundException.class);
  }
  
  @Test
  void getBankAccount_givenCorrectAccountNumberAndPin_shouldReturnAccount() {
    
    BankAccount bankAccount = new BankAccount(123456789L, 1234, BigDecimal.valueOf(200), BigDecimal.valueOf(100));
    Optional<BankAccount> accountFromRepository = Optional.of(bankAccount);
    when(repository.findByAccountNumber(123456789L)).thenReturn(accountFromRepository);
    
    BankAccount accountResult = service.getBankAccount(123456789L, 1234);
    
    assertThat(accountResult).isNotNull();
    assertThat(accountResult.getAccountNumber()).isEqualTo(123456789L);
    assertThat(accountResult.getPin()).isEqualTo(1234);
    assertThat(accountResult.getBalance()).isEqualTo(BigDecimal.valueOf(200));
    assertThat(accountResult.getOverdraft()).isEqualTo(BigDecimal.valueOf(100));
  }
  
  @Test
  void withdraw_ifAmountLessThanOrEqualsToZero_shouldThrowExceptionAndNotChangeBalance() {
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(200), BigDecimal.valueOf(100));
    
    assertThatThrownBy( () -> service.withdraw(account , BigDecimal.valueOf(-1))).isInstanceOf(InvalidAmountException.class);
    
    assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(200));
    assertThat(account.getOverdraft()).isEqualTo(BigDecimal.valueOf(100));
    
    assertThatThrownBy( () -> service.withdraw(account , BigDecimal.valueOf(0))).isInstanceOf(InvalidAmountException.class);
    
    assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(200));
    assertThat(account.getOverdraft()).isEqualTo(BigDecimal.valueOf(100));
  }
   
  @Test
  void withdraw_ifBankAccountHasNotEnoughBalance_shouldThrowExceptionAndNotChangeBalance() {
    
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(1000), BigDecimal.valueOf(100));
    
    assertThatThrownBy( () -> service.withdraw(account , BigDecimal.valueOf(2000)))
      .isInstanceOf(InsufficientBankAccountFundsException.class);
    
    assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(1000));
  }
  
  @Test
  void withdraw_ifAccountHasEnoughBalanceAndAmountGreaterThanZero_shouldWithdrawAmount_returnNewBalance() {
    
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(200), BigDecimal.valueOf(100));
    BigDecimal newBalance = service.withdraw(account , BigDecimal.valueOf(50));
    assertThat(newBalance).isEqualByComparingTo(BigDecimal.valueOf(150));
  }

  @Test
  void accountHasEnoughFunds_IfAmountIsLessThanOrEqualToFundsAvailable_shouldReturnTrue() {
    
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(200), BigDecimal.valueOf(100));

    assertThat(service.accountHasEnoughFunds(BigDecimal.TEN, account)).isTrue();
    assertThat(service.accountHasEnoughFunds(BigDecimal.valueOf(300), account)).isTrue();
  }
  
  @Test
  void accountHasEnoughFunds_IfAmountIsGreaterThanFundsAvailable_shouldReturnFalse() {
    
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(200), BigDecimal.valueOf(100));

    assertThat(service.accountHasEnoughFunds(BigDecimal.valueOf(301), account)).isFalse();
  }
  
  @Test
  void getTotalFundsAvailable_givenAnAccount_shouldReturnSumOfBalanceAndOverdraft() {
    
    BankAccount account = new BankAccount(1L, 1, BigDecimal.valueOf(200), BigDecimal.valueOf(100));

    assertThat(service.getTotalFundsAvailable(account)).isEqualByComparingTo(BigDecimal.valueOf(300));
  }
}
