package com.project.phonecaseshop.service;

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

    public String createReview(ReviewRequestDto reviewRequestDto, int productId) throws ParseException {
        // 그리고 추가적으로 상품을 구매한 사람인지 확인과정도 거쳐야 한다.
        // 현재 문제 상황 사용자가 아니더라도 id 를 입력하면 아무나 다 입력가능 그러면 토큰에 있는 email 과 해당 이메일로 찾은 사용자의 email과 같아야한다.
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
            return "리뷰가 등록되었습니다";
        }
        return "실패하였습니다";
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
        // 예외처리
        return null;
    }

    public String updateReview(int reviewId, ReviewRequestDto reviewRequestDto) {
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
            return "리뷰가 수정되었습니다";
        }
        return "리뷰 수정에 실패하였습니다";
    }

    public String deleteReview(int reviewId) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberEmail(currentMemberId);

        Optional<Review> review = reviewRepository.findById(reviewId);
        System.out.println("review = " + review.toString());

        if (member != null && review.isPresent()) {
            reviewRepository.deleteById(reviewId);
            return "리뷰가 삭제되었습니다";
        }
        return "리뷰 삭제에 실패하였습니다";
    }

    // ===================================================
    public ReviewResponseDto getReview(int reviewId) {
        // 해당 리뷰가 수정하려는 회원과 같은 사람인지 확인해야함
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findByMemberEmail(currentMemberId);

        if (findMember != null) {
            Optional<Review> review = reviewRepository.findById(reviewId);
            if (review.isPresent()) {
                return ReviewResponseDto.builder()
                        .reviewId(reviewId)
                        .reviewName(review.get().getReviewName())
                        .reviewContent(review.get().getReviewContent())
                        .reviewDate(review.get().getReviewDate())
                        .memberId(findMember)
                        .productId(review.get().getProductId())
                        .build();
            }
        }
        // 예외 처리
        return null;
    }
}
