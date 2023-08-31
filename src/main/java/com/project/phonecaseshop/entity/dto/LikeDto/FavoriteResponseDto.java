package com.project.phonecaseshop.entity.dto.LikeDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteResponseDto {
    private int memberId;
    private int productId;
}
