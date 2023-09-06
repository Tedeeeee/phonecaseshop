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
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Column
    private int orderCount;

    @Column
    private String productDesign;

    @Column
    private String productModel;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member memberId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product productId;
}
