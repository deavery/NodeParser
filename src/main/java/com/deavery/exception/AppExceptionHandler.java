package com.deavery.exception;

import com.deavery.exception.runtime.IllegalAccressException;
import com.deavery.exception.runtime.IllegalParamsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalParamsException.class)
    public ResponseEntity<String> handlePaymentIllegalData(IllegalParamsException illegalParamsException){
        return new ResponseEntity<String>("Illegal params!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalAccressException.class)
    public ResponseEntity<String> handlePaymentIllegalData(IllegalAccressException illegalAccressException){
        return new ResponseEntity<String>("Illegal access!", HttpStatus.BAD_REQUEST);
    }

}
