package com.project.phonecaseshop.repository;

import com.project.phonecaseshop.entity.RefreshToken;
import com.project.phonecaseshop.entity.dto.refreshToken.RefreshTokenDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshTokenDto> findByMemberId(Long memberId);
}
