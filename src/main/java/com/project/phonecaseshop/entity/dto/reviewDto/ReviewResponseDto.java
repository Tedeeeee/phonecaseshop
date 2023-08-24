package com.project.phonecaseshop.entity.dto.reviewDto;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReviewResponseDto {
    private int reviewId;
    private String reviewName;
    private String reviewContent;
    private String reviewDate;
    private Product productId;
    private Member memberId;
}
