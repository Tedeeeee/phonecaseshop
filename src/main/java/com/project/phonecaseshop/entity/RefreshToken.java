package com.project.phonecaseshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int refreshTokenId;

    @Column
    private int memberId;

    @Column(length = 200)
    private String refreshToken;
}
