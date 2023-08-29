package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.dto.reviewDto.ReviewRequestDto;
import com.project.phonecaseshop.entity.dto.reviewDto.ReviewResponseDto;
import com.project.phonecaseshop.responseApi.*;
import com.project.phonecaseshop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ApiResponse apiResponse;
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/{productId}/new")
    public CommonResult createReview(@RequestBody ReviewRequestDto reviewRequestDto
                                     , @PathVariable int productId) throws ParseException {

        String result = reviewService.createReview(reviewRequestDto, productId);

        if (result.equals("리뷰가 등록되었습니다")) {
            return apiResponse.getSuccessResult(1);
        }
        return apiResponse.getFailResult("500", result);
    }

    // 리뷰 한개 가져오기 ( 수정할때 사용할 예정 )
    @GetMapping("/{reviewId}")
    public SingleResult<ReviewResponseDto> getReview(@PathVariable int reviewId) {
        return apiResponse.getSingleResult(reviewService.getReview(reviewId));
    }

    // 리뷰 전체 가져오기
    @GetMapping("/list/{productId}")
    public PageResult<ReviewResponseDto> getAllReview(@PathVariable int productId,
                                                      @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return apiResponse.getPageResult(reviewService.getAllReview(productId, pageable));
    }

    // 리뷰 수정하기
    @PutMapping("/{reviewId}")
    public CommonResult updateReview(@PathVariable int reviewId, @RequestBody ReviewRequestDto reviewRequestDto) {
        String result = reviewService.updateReview(reviewId, reviewRequestDto);

        if (result.equals("리뷰가 수정되었습니다")) {
            return apiResponse.getSuccessResult(1);
        }
        return apiResponse.getFailResult("500", result);
    }

    // 리뷰 삭제하기
    @DeleteMapping("/{reviewId}")
    public CommonResult deleteReview(@PathVariable int reviewId) {
        String result = reviewService.deleteReview(reviewId);

        if (result.equals("리뷰가 삭제되었습니다")) {
            return apiResponse.getSuccessResult(1);
        }
        return apiResponse.getFailResult("500", result);
    }
}
