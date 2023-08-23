package com.project.phonecaseshop.entity;

import com.project.phonecaseshop.entity.dto.memberDto.MemberRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@DynamicUpdate
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column
    private int productPrice;

    @Column(length = 30)
    private String productName;

    @Column
    private int productDiscount;

    @Column
    private int productDeliveryPrice;

}
