package com.example.loanapp.exeptions;

import java.util.Date;

public class EmailError {

    private Integer errorCode;
    private String errorDate;

    private Date date;

    public EmailError(Integer errorCode, String errorDate, Date date) {
        this.errorCode = errorCode;
        this.errorDate = errorDate;
        this.date = date;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(String errorDate) {
        this.errorDate = errorDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "EmailError{" +
                "errorCode=" + errorCode +
                ", errorDate='" + errorDate + '\'' +
                ", date=" + date +
                '}';
    }
}
