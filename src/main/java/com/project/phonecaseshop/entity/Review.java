package com.project.phonecaseshop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@DynamicUpdate
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String reviewName;

    @Column
    private String reviewContent;

    @Column
    private String reviewPhoto;

    @Column
    private Date reviewDate;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member memberId;
}
