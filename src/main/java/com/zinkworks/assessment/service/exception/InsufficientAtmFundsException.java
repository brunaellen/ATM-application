package com.zinkworks.assessment.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "ATM has insuficient funds to process the operation")
public class InsufficientAtmFundsException extends RuntimeException {
  private static final long serialVersionUID = 1L;
}
