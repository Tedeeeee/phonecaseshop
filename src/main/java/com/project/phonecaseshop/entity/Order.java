package com.project.phonecaseshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Builder
public class Order {
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
