package com.project.phonecaseshop.entity.dto.MemberDto;

import lombok.Data;

@Data
public class MemberRequestDto {
    private Long memberId;
    private String memberEmail;
    private String memberPassword;
    private String memberNickname;
    private Long memberPoint;
    private String memberAddress;
    private String memberDetailAddress;
    private String memberStatus;
}
