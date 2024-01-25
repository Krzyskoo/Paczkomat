package com.example.loanapp.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class EmailExceptionHandler {

    @ExceptionHandler(value = EmailExceptionByClient.class)
    public ResponseEntity<EmailError> handleNoEmailFoundException(){
        EmailError emailError =new EmailError(400,"wrong recipient's e-mail ", new Date());
        return new ResponseEntity<>(emailError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = EmailExceptionByServer.class)
    public ResponseEntity<EmailError> handleEmailReminder(){
        EmailError emailError =new EmailError(500,"Sending Reminder Email wrong ", new Date());
        return new ResponseEntity<>(emailError, HttpStatus.BAD_REQUEST);
    }
}
