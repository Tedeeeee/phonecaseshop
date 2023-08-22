package com.project.phonecaseshop.entity.dto.memberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long memberId;
    private String memberEmail;
    private String memberPassword;
    private String memberNickname;
    private Long memberPoint;
    private String memberAddress;
    private String memberDetailAddress;
    private String memberStatus;
}
