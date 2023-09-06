package com.project.phonecaseshop.entity.dto.reviewDto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ReviewRequestDto {
    private String reviewName;
    private String reviewContent;
    private String reviewDate;
}
