package com.project.phonecaseshop.config.exception;

import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponse apiResponse;

    @ExceptionHandler(BusinessExceptionHandler.class)
    protected CommonResult handleBusinessExceptionHandler(BusinessExceptionHandler ex) {
        return apiResponse.getFailResult(ErrorCode.BUSINESS_EXCEPTION_ERROR.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected CommonResult handlerAllException(Exception ex) {
        return apiResponse.getFailResult(ErrorCode.INVALID_TYPE_VALUE.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    protected CommonResult handleForbiddenHandler(HttpClientErrorException.Forbidden ex) {
        return apiResponse.getFailResult(ErrorCode.FORBIDDEN_ERROR.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    protected CommonResult handlerIOException(IOException ex) {
        return apiResponse.getFailResult(ErrorCode.IO_ERROR.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    protected CommonResult handlerNullPointerException(NullPointerException ex) {
        return apiResponse.getFailResult(ErrorCode.NULL_POINT_ERROR.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected CommonResult handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return apiResponse.getFailResult(ErrorCode.REQUEST_BODY_MISSING_ERROR.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected CommonResult handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return apiResponse.getFailResult(ErrorCode.REQUEST_BODY_MISSING_ERROR.getStatus(), ex.getMessage());
    }
}
