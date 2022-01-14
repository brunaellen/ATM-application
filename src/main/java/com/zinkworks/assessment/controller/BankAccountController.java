package com.zinkworks.assessment.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zinkworks.assessment.controller.dto.BankAccountBalanceRequest;
import com.zinkworks.assessment.controller.dto.BankAccountBalanceResponse;
import com.zinkworks.assessment.controller.dto.BankAccountRequest;
import com.zinkworks.assessment.controller.dto.BankAccountResponse;
import com.zinkworks.assessment.controller.dto.BankAccountWithdrawRequest;
import com.zinkworks.assessment.controller.dto.BankAccountWithdrawResponse;
import com.zinkworks.assessment.model.BankAccount;
import com.zinkworks.assessment.service.BankAccountService;
import com.zinkworks.assessment.service.WithdrawTransactionService;

@RestController
@RequestMapping("/bankAccount")
public class BankAccountController {
  
  @Autowired
  private BankAccountService bankAccountService;
  
  @Autowired
  private WithdrawTransactionService withdrawService;
  
  @PostMapping("/details")
  public BankAccountResponse getAccount(@RequestBody BankAccountRequest request) {
    BankAccount account = bankAccountService.getBankAccount(request.getAccountNumber(), request.getPin());
    BigDecimal totalFundsAvailable = bankAccountService.getTotalFundsAvailable(account);
    return new BankAccountResponse(account, totalFundsAvailable);
  }
  
  @PostMapping("/balance")
  public BankAccountBalanceResponse balance(@RequestBody BankAccountBalanceRequest request) {
    BankAccount account = bankAccountService.getBankAccount(request.getAccountNumber(), request.getPin());
    BigDecimal totalFundsAvailable = bankAccountService.getTotalFundsAvailable(account);
    return new BankAccountBalanceResponse(
        account.getBalance(), 
        totalFundsAvailable);
  }
  
  @PostMapping("/withdraw")
  public BankAccountWithdrawResponse withdraw(@RequestBody BankAccountWithdrawRequest request) {
    Long id = 1L;
    Map<Integer,Integer> notesToBeDispensed = withdrawService.withdraw(request.getAccountNumber(), request.getPin(), request.getAmount(), id);
    BankAccount account = bankAccountService.getBankAccount(request.getAccountNumber(), request.getPin());
    BigDecimal balance = account.getBalance();
    BigDecimal overdraft = account.getOverdraft();
    
    return new BankAccountWithdrawResponse(request.getAccountNumber(), notesToBeDispensed, balance, overdraft);
  }
}
