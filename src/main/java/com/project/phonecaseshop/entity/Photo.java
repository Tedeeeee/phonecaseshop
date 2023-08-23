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
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int photoId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product productId;

    @Column(length = 30)
    private String photoName;

    public Photo(String photoName) {
        this.photoName = photoName;
    }
}
