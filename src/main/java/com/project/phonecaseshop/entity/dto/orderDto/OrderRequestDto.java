package com.project.phonecaseshop.entity.dto.orderDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class OrderRequestDto {
    private int orderId;
    private int orderCount;
    private String productDesign;
    private String productModel;
    private int productId;
}
