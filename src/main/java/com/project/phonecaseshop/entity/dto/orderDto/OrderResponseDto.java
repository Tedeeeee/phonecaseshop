package com.project.phonecaseshop.entity.dto.orderDto;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDto {
    private int orderId;
    private int orderCount;
    private String productDesign;
    private String productModel;
    private Member member;
    private Product productId;
    private Member memberId;
}
