package com.project.phonecaseshop.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityUtil {
    public SecurityUtil() {
    }

    public static String getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("로그인을 다시 확인해주세요");
        }
        return authentication.getName();
    }

}
