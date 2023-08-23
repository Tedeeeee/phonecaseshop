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
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int modelId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product productId;

    @Column(length = 30)
    private String modelName;

    public Model(String modelName) {
        this.modelName = modelName;
    }
}
