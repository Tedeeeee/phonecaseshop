package com.project.phonecaseshop.config.exception;

import lombok.Builder;
import lombok.Getter;

public class BusinessExceptionHandler extends RuntimeException{

    @Getter
    private final ErrorCode errorCode;

    @Builder
    public BusinessExceptionHandler(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
