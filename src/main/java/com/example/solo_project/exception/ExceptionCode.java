package com.example.solo_project.exception;

import lombok.Getter;

public enum ExceptionCode {
    TODO_NOT_FOUND(404, "Todo Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    TODO_EXIST(409, "Todo Exist"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");
    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status,String message) {
        this.status = status;
        this.message = message;
    }
}
