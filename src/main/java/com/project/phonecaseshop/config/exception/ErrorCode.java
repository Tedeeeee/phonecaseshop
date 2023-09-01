package com.project.phonecaseshop.config.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public enum ErrorCode {

    BUSINESS_EXCEPTION_ERROR(200, "서비스에서 문제가 발생"),

    REQUEST_BODY_MISSING_ERROR(400, "@Request Body 에 데이터가 존재하지 않는다"),

    FORBIDDEN_ERROR(403, "해당 사용자에게 권한이 없다"),

    INVALID_TYPE_VALUE(400, "유효하지 않은 타입이다."),

    IO_ERROR(400, "입/출력값이 옮바르지 않습니다"),
 
    NULL_POINT_ERROR(404, "Null 발생");

    private int status;
    private String message;
}
