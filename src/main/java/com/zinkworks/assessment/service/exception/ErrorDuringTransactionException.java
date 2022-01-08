package com.zinkworks.assessment.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Somthing went wrong, please try again in few minutes")
public class ErrorDuringTransactionException extends RuntimeException {
  private static final long serialVersionUID = 1L;
}
