package com.zinkworks.assessment.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Pin is invalid")
public class InvalidPinException extends RuntimeException {
  private static final long serialVersionUID = 1L;
}
