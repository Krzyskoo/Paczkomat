package com.example.loanapp.exeptions;

public class EmailExceptionByServer extends RuntimeException {
    public EmailExceptionByServer(String message) {
        super(message);
    }

    public EmailExceptionByServer() {
    }
}
