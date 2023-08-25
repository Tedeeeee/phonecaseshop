package com.project.phonecaseshop.service;

import com.project.phonecaseshop.entity.*;
import com.project.phonecaseshop.entity.dto.productDto.ProductResponseDto;
import com.project.phonecaseshop.entity.dto.reviewDto.ReviewRequestDto;
import com.project.phonecaseshop.entity.dto.reviewDto.ReviewResponseDto;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.ProductRepository;
import com.project.phonecaseshop.repository.ReviewRepository;
import com.project.phonecaseshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public String createReview(ReviewRequestDto reviewRequestDto, int memberId, int productId) throws ParseException {
        // 그리고 추가적으로 상품을 구매한 사람인지 확인과정도 거쳐야 한다.
        // 현재 문제 상황 사용자가 아니더라도 id 를 입력하면 아무나 다 입력가능 그러면 토큰에 있는 email 과 해당 이메일로 찾은 사용자의 email과 같아야한다.
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findByMemberEmail(currentMemberId);

        Optional<Product> product = productRepository.findById(productId);
        Optional<Member> member = memberRepository.findById(memberId);

        if (product.isPresent() && member.isPresent() && findMember.getMemberId() == memberId) {
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
        // 해당 리뷰가 수정하려는 회원과 같은 사람인지 확인해야함
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findByMemberEmail(currentMemberId);

        Optional<Product> product = productRepository.findById(productId);
        Optional<Member> member = memberRepository.findById(memberId);

        if (product.isPresent() && member.isPresent() && findMember.getMemberId() == memberId) {
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

    public List<ReviewResponseDto> getAllReview(int productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            List<Review> reviews = reviewRepository.findByProductId(product.get());

            return reviews.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
        }
        // 예외처리
        return null;
    }

    private ReviewResponseDto convertToResponseDto(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .reviewName(review.getReviewName())
                .reviewContent(review.getReviewContent())
                .reviewDate(review.getReviewDate())
                .productId(review.getProductId())
                .memberId(review.getMemberId())
                .build();
    }
}
