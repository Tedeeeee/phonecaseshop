package com.project.phonecaseshop.entity.dto.productDto;

import com.project.phonecaseshop.entity.Design;
import com.project.phonecaseshop.entity.Model;
import com.project.phonecaseshop.entity.Photo;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
public class ProductResponseDto {
    private int productId;
    private String memberEmail;
    private String productName;
    private int productPrice;
    private int productDiscount;
    private int productDeliveryPrice;
    private List<Model> productModel;
    private List<Design> productDesign;
    private List<Photo> productPhoto;
}
