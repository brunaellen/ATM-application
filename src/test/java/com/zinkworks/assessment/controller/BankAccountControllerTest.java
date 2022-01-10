package com.zinkworks.assessment.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.service.BankAccountService;
import com.zinkworks.assessment.service.WithdrawTransactionService;
import com.zinkworks.assessment.service.exception.AccountNotFoundException;
import com.zinkworks.assessment.service.exception.InsufficientAtmFundsException;
import com.zinkworks.assessment.service.exception.InsufficientBankAccountFundsException;
import com.zinkworks.assessment.service.exception.InvalidPinException;


@WebMvcTest(controllers = {BankAccountController.class})
class BankAccountControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private BankAccountService bankAccountService;
  @MockBean
  private WithdrawTransactionService withdrawService;
  
  @Test
  void getAccount_givenBankAccountAndPin_shouldReturnAccountDetails() throws Exception {
    when(bankAccountService.getBankAccount(1L, 1234))
    .thenReturn(new BankAccount(1L, 1234, BigDecimal.valueOf(100), BigDecimal.valueOf(100)));
  
    final String balanceRequest = "{\n"
        + "    \"accountNumber\": 1,\n"
        + "    \"pin\": 1234\n"
        + "}";
    
    mockMvc
      .perform(post("/bankAccount/details")
          .content(balanceRequest)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.balance").value(100))
      .andExpect(jsonPath("$.availableToSpend").value(200))
      .andExpect(jsonPath("$.accountNumber").value(1L))
      .andExpect(jsonPath("$.statements").isEmpty());
  }

  @Test
  void balance_givenBankAccountAndPin_shouldReturnAccountBalance() throws Exception {
    
    when(bankAccountService.getBankAccount(1L, 1234))
      .thenReturn(new BankAccount(1L, 1234, BigDecimal.valueOf(100), BigDecimal.valueOf(100)));
    
    final String balanceRequest = "{\n"
        + "    \"accountNumber\": 1,\n"
        + "    \"pin\": 1234\n"
        + "}";
    
    mockMvc
      .perform(post("/bankAccount/balance")
          .content(balanceRequest)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.balance").value(100))
      .andExpect(jsonPath("$.totalFundsForWithdraw").value(200));
  }
  
  @Test
  void balance_givenBankAccountAndBadPin_shouldReturn403() throws Exception {
    
    when(bankAccountService.getBankAccount(1L, 1234))
      .thenThrow(InvalidPinException.class);
    
    final String balanceRequest = "{\n"
        + "    \"accountNumber\": 1,\n"
        + "    \"pin\": 1234\n"
        + "}";
    
    mockMvc
      .perform(post("/bankAccount/balance")
          .content(balanceRequest)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isForbidden());
  }
  
  @Test
  void balance_givenBankAccountDoesntExist_shouldReturn404() throws Exception {
    
    when(bankAccountService.getBankAccount(1L, 1234))
      .thenThrow(AccountNotFoundException.class);
    
    final String balanceRequest = "{\n"
        + "    \"accountNumber\": 1,\n"
        + "    \"pin\": 1234\n"
        + "}";
    
    mockMvc
      .perform(post("/bankAccount/balance")
          .content(balanceRequest)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isNotFound());
  }
  
  @Test
  void withdraw_givenServiceProcessOperation_shouldReturn200() throws Exception {
    
    when(withdrawService.withdraw(1L, 1234, BigDecimal.valueOf(200), 1L))
      .thenReturn(Map.of(50, 4, 20, 0, 10, 0, 5, 0));
    
    when(bankAccountService.getBankAccount(1L, 1234))
      .thenReturn(new BankAccount(1L, 1234, BigDecimal.valueOf(200), BigDecimal.valueOf(100)));
    
    final String balanceRequest = "{\n"
        + "    \"accountNumber\": 1,\n"
        + "    \"pin\": 1234,\n"
        + "    \"amount\": 200\n"
        + "}";
    
    mockMvc
      .perform(post("/bankAccount/withdraw")
          .content(balanceRequest)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.notes.50").value(4))
      .andExpect(jsonPath("$.accountNumber").value(1))
      .andExpect(jsonPath("$.balance").value(200));
  }
  
  @Test
  void withdraw_givenAccountHasNotEnoughMoney_shouldReturn400() throws Exception {
    
    when(withdrawService.withdraw(1L, 1234, BigDecimal.valueOf(200), 1L))
      .thenThrow(InsufficientBankAccountFundsException.class);
    
    final String balanceRequest = "{\n"
        + "    \"accountNumber\": 1,\n"
        + "    \"pin\": 1234,\n"
        + "    \"amount\": 200\n"
        + "}";
    
    mockMvc
      .perform(post("/bankAccount/withdraw")
          .content(balanceRequest)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }
  
  
  @Test
  void withdraw_givenATMHasNotEnoughMoney_shouldReturn400() throws Exception {
    
    when(withdrawService.withdraw(1L, 1234, BigDecimal.valueOf(200), 1L))
      .thenThrow(InsufficientAtmFundsException.class);
    
    final String balanceRequest = "{\n"
        + "    \"accountNumber\": 1,\n"
        + "    \"pin\": 1234,\n"
        + "    \"amount\": 200\n"
        + "}";
    
    mockMvc
      .perform(post("/bankAccount/withdraw")
          .content(balanceRequest)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }
}
