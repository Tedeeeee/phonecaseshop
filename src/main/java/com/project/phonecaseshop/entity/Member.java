package com.project.phonecaseshop.entity;

import com.project.phonecaseshop.entity.dto.memberDto.MemberRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@DynamicUpdate
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberId;

    @Column(length = 30)
    private String memberEmail;

    @Column(length = 100)
    private String memberPassword;

    @Column(length = 12)
    private String memberNickname;

    @Column
    private int memberPoint;

    @Column(length = 300)
    private String memberAddress;

    @Column(length = 30)
    private String memberDetailAddress;

    // 단일 문자의 경우 별도의 설정 X
    @Column
    private String memberStatus;

    public void updateProfile(MemberRequestDto memberRequestDto) {
        this.memberNickname = memberRequestDto.getMemberNickname();
        this.memberAddress = memberRequestDto.getMemberAddress();
        this.memberDetailAddress = memberRequestDto.getMemberDetailAddress();
    }
}
