package com.project.phonecaseshop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
