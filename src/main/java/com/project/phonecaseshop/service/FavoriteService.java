package com.project.phonecaseshop.service;

import com.project.phonecaseshop.config.exception.BusinessExceptionHandler;
import com.project.phonecaseshop.config.exception.ErrorCode;
import com.project.phonecaseshop.entity.Favorite;
import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.Product;
import com.project.phonecaseshop.entity.dto.LikeDto.FavoriteResponseDto;
import com.project.phonecaseshop.repository.FavoriteRepository;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.ProductRepository;
import com.project.phonecaseshop.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public int insertLike(int productId) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberEmail(currentMemberId);
        Optional<Product> product = productRepository.findById(productId);

        if (member != null && product.isPresent()) {
            List<Favorite> findMemberId = favoriteRepository.findByMemberId_MemberId(member.getMemberId());

            boolean alreadyLiked = findMemberId.stream()
                    .anyMatch(favorite -> favorite.getProductId().getProductId() == productId);
            if (alreadyLiked) {
                throw new BusinessExceptionHandler("이미 찜한 상품입니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
            }
            Favorite favorite = Favorite.builder()
                    .memberId(member)
                    .productId(product.get())
                    .build();
            favoriteRepository.save(favorite);
            return 1;
        }
        throw new BusinessExceptionHandler("찜을 실패하였습니다.", ErrorCode.BUSINESS_EXCEPTION_ERROR);
    }

    @Transactional
    public int deleteLike(int productId) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberEmail(currentMemberId);
        Optional<Product> product = productRepository.findById(productId);

        if (member != null && product.isPresent()) {
            favoriteRepository.deleteByProductId_ProductId(productId);
            return 1;
        }

        throw new BusinessExceptionHandler("찜 취소가 실패하였습니다.", ErrorCode.BUSINESS_EXCEPTION_ERROR);
    }

    // 가져오는 로직
    public List<FavoriteResponseDto> findLike() {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberEmail(currentMemberId);

        if (member != null) {
            List<Favorite> favorites = favoriteRepository.findByMemberId_MemberId(member.getMemberId());

            return favorites.stream()
                    .map(favorite -> FavoriteResponseDto.builder()
                            .memberId(favorite.getMemberId().getMemberId())
                            .productId(favorite.getProductId().getProductId())
                            .build()
                    ).collect(Collectors.toList());
        } else {
            throw new BusinessExceptionHandler("로그인 정보가 존재하지 않습니다.", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
    }
}
