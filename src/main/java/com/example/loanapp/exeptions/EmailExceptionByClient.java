package com.example.loanapp.exeptions;


public class EmailExceptionByClient extends RuntimeException {

    public EmailExceptionByClient(String message) {
        super(message);
    }

    public EmailExceptionByClient() {
    }
}
