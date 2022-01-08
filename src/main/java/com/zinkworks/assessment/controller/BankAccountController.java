package com.zinkworks.assessment.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
  @ResponseBody
  public BankAccountResponse getAccount(@RequestBody BankAccountRequest request) {
    BankAccount account = bankAccountService.getBankAccount(request.getAccountNumber(), request.getPin());
    return new BankAccountResponse(account);
  }
  
  @PostMapping("/balance")
  @ResponseBody
  public BankAccountBalanceResponse balance(@RequestBody BankAccountBalanceRequest request) {
    BankAccount account = bankAccountService.getBankAccount(request.getAccountNumber(), request.getPin());
    return new BankAccountBalanceResponse(
        account.getCurrentBalance(),
        account.getTotalFundsAvailable());
  }
  
  @PostMapping("/withdraw")
  @ResponseBody
  public BankAccountWithdrawResponse withdraw(@RequestBody BankAccountWithdrawRequest request) {
    Map<Integer,Integer> withdraw = withdrawService.withdraw(request.getAccountNumber(), request.getPin(), request.getAmount());
    BankAccount account = bankAccountService.getBankAccount(request.getAccountNumber(), request.getPin());
    BigDecimal balance = account.getCurrentBalance();
    
    return new BankAccountWithdrawResponse(request.getAccountNumber(), withdraw, balance);
  }
}
