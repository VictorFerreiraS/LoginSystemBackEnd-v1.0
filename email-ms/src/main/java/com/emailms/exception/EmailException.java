package com.emailms.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailException extends Exception{
    String message;

    @Override
    public String getMessage() {
        return message;
    }
}
