package com.project.phonecaseshop.entity.dto.productDto;

import com.project.phonecaseshop.entity.Design;
import com.project.phonecaseshop.entity.Model;
import com.project.phonecaseshop.entity.Photo;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ProductResponseDto {
    private Long productId;
    private String memberEmail;
    private String productName;
    private Long productPrice;
    private String productDiscount;
    private String productDeliveryPrice;
    private List<Model> productModel;
    private List<Design> productDesign;
    private List<Photo> productPhoto;
}
