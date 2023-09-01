package com.project.phonecaseshop.config.exception;

import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponse apiResponse;

    @ExceptionHandler(BusinessExceptionHandler.class)
    protected CommonResult handleBusinessExceptionHandler(BusinessExceptionHandler bx) {
        return apiResponse.getFailResult(ErrorCode.BUSINESS_EXCEPTION_ERROR.getStatus(), bx.getMessage());
    }
}
