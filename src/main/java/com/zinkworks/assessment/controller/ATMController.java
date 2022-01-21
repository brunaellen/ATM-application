package com.zinkworks.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zinkworks.assessment.controller.dto.ATMTransactionsResponse;
import com.zinkworks.assessment.model.Atm;
import com.zinkworks.assessment.service.ATMService;

@RestController
@RequestMapping("/atm")
public class ATMController {
  
  @Autowired
  private ATMService atmService;
  
  @GetMapping("/transactions")
  public ATMTransactionsResponse getATMTransactions() {
    Long id = 1L;
    Atm atm = atmService.getAtm(id);
    ATMTransactionsResponse transactionResponse = new ATMTransactionsResponse(atm);
    transactionResponse.setStatements(atm.getNotesAvailable());
    return transactionResponse;
  }
}
