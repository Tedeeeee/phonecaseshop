package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.dto.LikeDto.FavoriteResponseDto;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import com.project.phonecaseshop.responseApi.ListResult;
import com.project.phonecaseshop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final ApiResponse apiResponse;
    private final FavoriteService favoriteService;

    // 찜하기
    @GetMapping("/{productId}")
    public CommonResult insertLike(@PathVariable int productId) {
        return apiResponse.getSuccessResult(favoriteService.insertLike(productId));
    }

    // 찜 취소
    @DeleteMapping("/{productId}")
    public CommonResult deleteLike(@PathVariable int productId) {
        return apiResponse.getSuccessResult(favoriteService.deleteLike(productId));
    }

    // 찜한 상품 가져오기
    @GetMapping("")
    public ListResult<FavoriteResponseDto> findLike() {
        return apiResponse.getListResult(favoriteService.findLike());
    }
}
