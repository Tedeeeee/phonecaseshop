package com.project.phonecaseshop.service;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.Product;
import com.project.phonecaseshop.entity.Review;
import com.project.phonecaseshop.entity.dto.reviewDto.ReviewRequestDto;
import com.project.phonecaseshop.entity.dto.reviewDto.ReviewResponseDto;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.ProductRepository;
import com.project.phonecaseshop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public String createReview(ReviewRequestDto reviewRequestDto, int memberId, int productId) throws ParseException {
        Optional<Product> product = productRepository.findById(productId);
        Optional<Member> member = memberRepository.findById(memberId);

        if (product.isPresent() && member.isPresent()) {
            Review review = Review.builder()
                    .reviewName(reviewRequestDto.getReviewName())
                    .reviewContent(reviewRequestDto.getReviewContent())
                    .reviewDate(reviewRequestDto.getReviewDate())
                    .memberId(member.get())
                    .productId(product.get())
                    .build();

            reviewRepository.save(review);
            return "리뷰가 등록되었습니다";
        }
        return "실패하였습니다";
    }

    public ReviewResponseDto getReview(int memberId, int productId, int reviewId) {
        Optional<Product> product = productRepository.findById(productId);
        Optional<Member> member = memberRepository.findById(memberId);

        if (product.isPresent() && member.isPresent()) {
            Optional<Review> review = reviewRepository.findById(reviewId);
            if (review.isPresent()) {
                return ReviewResponseDto.builder()
                        .reviewId(reviewId)
                        .reviewName(review.get().getReviewName())
                        .reviewContent(review.get().getReviewContent())
                        .reviewDate(review.get().getReviewDate())
                        .memberId(member.get())
                        .productId(product.get())
                        .build();
            }
        }
        // 예외 처리
        return null;
    }
}
