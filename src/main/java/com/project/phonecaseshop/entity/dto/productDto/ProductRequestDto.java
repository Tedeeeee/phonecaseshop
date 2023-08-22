package com.project.phonecaseshop.entity.dto.productDto;

import com.project.phonecaseshop.entity.Design;
import com.project.phonecaseshop.entity.Model;
import com.project.phonecaseshop.entity.Photo;
import lombok.Data;

import java.util.*;

@Data
public class ProductRequestDto {
    private String productName;
    private Long productPrice;
    private String productDiscount;
    private String productDeliveryPrice;
    private List<Model> productModel = new ArrayList<>();
    private List<Design> productDesign = new ArrayList<>();
    private List<Photo> productPhoto = new ArrayList<>();
}
