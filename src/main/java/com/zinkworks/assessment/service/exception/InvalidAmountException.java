package com.zinkworks.assessment.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidAmountException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InvalidAmountException() {
    super("There are no combination of notes to fulfil the request.");
  }
  
  public InvalidAmountException(String message) {
    super(message);
  }
}
