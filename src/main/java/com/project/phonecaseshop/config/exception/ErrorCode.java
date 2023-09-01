package com.project.phonecaseshop.config.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public enum ErrorCode {

    BUSINESS_EXCEPTION_ERROR(200, "Business Exception Error");

    private int status;
    private String message;
}
