package com.project.phonecaseshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column
    private String memberEmail;

    @Column
    private String memberPassword;

    @Column
    private String memberNickname;

    @Column
    private Long memberPoint;

    @Column
    private String memberAddress;

    @Column
    private String memberDetailAddress;

    @Column
    private String memberStatus;

}
