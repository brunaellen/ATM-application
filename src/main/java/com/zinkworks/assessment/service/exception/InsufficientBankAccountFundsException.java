package com.zinkworks.assessment.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Insuficient funds on the bank account.")
public class InsufficientBankAccountFundsException extends RuntimeException {
  private static final long serialVersionUID = 1L;
}
