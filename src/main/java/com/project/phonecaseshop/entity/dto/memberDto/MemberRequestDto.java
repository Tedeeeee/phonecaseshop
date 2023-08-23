package com.project.phonecaseshop.entity.dto.memberDto;

import lombok.Data;

@Data
public class MemberRequestDto {
    private int memberId;
    private String memberEmail;
    private String memberPassword;
    private String memberNickname;
    private int memberPoint;
    private String memberAddress;
    private String memberDetailAddress;
    private String memberStatus;
}
