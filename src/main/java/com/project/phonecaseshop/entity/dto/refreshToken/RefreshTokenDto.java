package com.project.phonecaseshop.entity.dto.refreshToken;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class RefreshTokenDto {
    private Long refreshTokenId;
    private Long memberId;
    private String refreshToken;
}
