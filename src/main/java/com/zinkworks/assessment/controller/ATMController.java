package com.zinkworks.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinkworks.assessment.controller.dto.ATMTransactionsResponse;
import com.zinkworks.assessment.model.Atm;
import com.zinkworks.assessment.service.ATMService;

@Controller
@RequestMapping("/atm")
public class ATMController {
  
  @Autowired
  private ATMService atmService;
  
  @GetMapping("/transactions")
  @ResponseBody
  public ATMTransactionsResponse getATMTransactions() {
    Long id = 1L;
    Atm atm = atmService.getAtm(id);
    return new ATMTransactionsResponse(atm);
  }
}
