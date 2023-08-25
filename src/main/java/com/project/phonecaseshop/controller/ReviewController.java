package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.dto.reviewDto.ReviewRequestDto;
import com.project.phonecaseshop.entity.dto.reviewDto.ReviewResponseDto;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import com.project.phonecaseshop.responseApi.ListResult;
import com.project.phonecaseshop.responseApi.SingleResult;
import com.project.phonecaseshop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ApiResponse apiResponse;
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/{memberId}/{productId}")
    public CommonResult createReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                     @PathVariable int memberId, @PathVariable int productId) throws ParseException {

        String result = reviewService.createReview(reviewRequestDto, memberId, productId);

        if (result.equals("리뷰가 등록되었습니다")) {
            return apiResponse.getSuccessResult(1);
        }
        return apiResponse.getFailResult("500", result);
    }

    // 리뷰 한개 가져오기 ( 수정할때 사용할 예정 )
    @GetMapping("{memberId}/{productId}/{reviewId}")
    public SingleResult<ReviewResponseDto> getReview(@PathVariable int memberId, @PathVariable int productId,
                                                     @PathVariable int reviewId) {
        return apiResponse.getSingleResult(reviewService.getReview(memberId, productId, reviewId));
    }

    // 리뷰 전체 가져오기
    @GetMapping("{productId}")
    public ListResult<ReviewResponseDto> getAllReview(@PathVariable int productId) {
        return apiResponse.getListResult(reviewService.getAllReview(productId));
    }
}
