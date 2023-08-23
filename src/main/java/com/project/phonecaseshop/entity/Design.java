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
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int designId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product productId;

    @Column(length = 30)
    private String designName;

    public Design(String designName) {
        this.designName = designName;
    }
}
