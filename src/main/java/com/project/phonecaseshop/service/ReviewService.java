package com.project.phonecaseshop.service;

import com.project.phonecaseshop.config.exception.BusinessExceptionHandler;
import com.project.phonecaseshop.config.exception.ErrorCode;
import com.project.phonecaseshop.entity.*;
import com.project.phonecaseshop.entity.dto.reviewDto.ReviewRequestDto;
import com.project.phonecaseshop.entity.dto.reviewDto.ReviewResponseDto;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.ProductRepository;
import com.project.phonecaseshop.repository.ReviewRepository;
import com.project.phonecaseshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public int createReview(ReviewRequestDto reviewRequestDto, int productId) throws ParseException {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findByMemberEmail(currentMemberId);

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent() && findMember != null) {
            Review review = Review.builder()
                    .reviewName(reviewRequestDto.getReviewName())
                    .reviewContent(reviewRequestDto.getReviewContent())
                    .reviewDate(reviewRequestDto.getReviewDate())
                    .memberId(findMember)
                    .productId(product.get())
                    .build();

            reviewRepository.save(review);
            return 1;
        }
        throw new BusinessExceptionHandler("상품생성을 실패했습니다.", ErrorCode.BUSINESS_EXCEPTION_ERROR);
    }

    public Page<ReviewResponseDto> getAllReview(int productId, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            if (pageSize == 5 || pageSize == 10 || pageSize == 50 || pageSize == 100) {
                PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageSize);
                Page<Review> productIds = reviewRepository.findByProductId(product.get(), pageRequest);

                return productIds.map(review -> {
                    return ReviewResponseDto.builder()
                            .reviewId(review.getReviewId())
                            .reviewName(review.getReviewName())
                            .reviewContent(review.getReviewContent())
                            .reviewDate(review.getReviewDate())
                            .memberId(review.getMemberId())
                            .productId(review.getProductId())
                            .build();
                });
            }
        }
        throw new BusinessExceptionHandler("상품이 존재하지 않습니다.", ErrorCode.BUSINESS_EXCEPTION_ERROR);
    }

    public int updateReview(int reviewId, ReviewRequestDto reviewRequestDto) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberEmail(currentMemberId);

        Optional<Review> findReview = reviewRepository.findById(reviewId);

        if (member != null && findReview.isPresent()) {
            Review review = Review.builder()
                    .reviewId(reviewId)
                    .reviewName(reviewRequestDto.getReviewName())
                    .reviewContent(reviewRequestDto.getReviewContent())
                    .reviewDate(reviewRequestDto.getReviewDate())
                    .memberId(findReview.get().getMemberId())
                    .productId(findReview.get().getProductId())
                    .build();

            reviewRepository.save(review);
            return 1;
        }
        throw new BusinessExceptionHandler("리뷰 수정에 실패하였습니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
    }

    public int deleteReview(int reviewId) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberEmail(currentMemberId);

        Optional<Review> review = reviewRepository.findById(reviewId);
        System.out.println("review = " + review.toString());

        if (member != null && review.isPresent()) {
            reviewRepository.deleteById(reviewId);
            return 1;
        }
        throw new BusinessExceptionHandler("리뷰 삭제에 실패하였습니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
    }
}
