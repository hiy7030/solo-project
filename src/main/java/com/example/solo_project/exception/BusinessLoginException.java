package com.example.solo_project.exception;

import lombok.Getter;

@Getter
public class BusinessLoginException extends RuntimeException{

    @Getter
    private ExceptionCode exceptionCode;

    public BusinessLoginException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
