package com.project.phonecaseshop.config.exception;

import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import com.project.phonecaseshop.service.SlackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponse apiResponse;
    private final SlackService slackService;

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

    @ExceptionHandler(BusinessExceptionHandler.class)
    protected CommonResult handleBusinessExceptionHandler(BusinessExceptionHandler ex) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초");
        String currentTimeStr = currentTime.format(formatter);

        String title = "BusinessException 에러 발생!";
        HashMap<String, String> data = new HashMap<>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestUrl = request.getRequestURL().toString();

        data.put("Request URL", requestUrl);
        data.put("발생 시간", currentTimeStr);
        data.put("status 코드", ErrorCode.BUSINESS_EXCEPTION_ERROR.getStatus());
        data.put("로그 메세지", ex.getMessage());

        slackService.sendMessage(title, data);
        return apiResponse.getFailResult(ErrorCode.BUSINESS_EXCEPTION_ERROR.getStatus(), ex.getMessage());
    }
}
