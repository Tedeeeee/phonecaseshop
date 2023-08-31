package com.project.phonecaseshop.service;

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

import java.util.Collections;
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
                // 이미 저장되어 있는 상품에 대한 예외 처리
                return 0;
            }
            Favorite favorite = Favorite.builder()
                    .memberId(member)
                    .productId(product.get())
                    .build();
            favoriteRepository.save(favorite);
            return 1;
        }

        // 예외처리 ( 실패함 )
        return 0;
    }

    @Transactional
    public int deleteLike(int productId) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberEmail(currentMemberId);
        Optional<Product> product = productRepository.findById(productId);

        if (member != null && product.isPresent()) {
            System.out.println("여기로 들어오는가?");
            favoriteRepository.deleteByProductId_ProductId(productId);
            return 1;
        }

        // 예외처리
        return 0;
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
        }
        return Collections.emptyList();
    }
}
