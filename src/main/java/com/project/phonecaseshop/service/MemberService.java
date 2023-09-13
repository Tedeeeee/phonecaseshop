package com.project.phonecaseshop.service;

import com.project.phonecaseshop.config.exception.BusinessExceptionHandler;
import com.project.phonecaseshop.config.exception.ErrorCode;
import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.RefreshToken;
import com.project.phonecaseshop.entity.dto.memberDto.MemberRequestDto;
import com.project.phonecaseshop.entity.dto.memberDto.MemberResponseDto;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.RefreshTokenRepository;
import com.project.phonecaseshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public int signUp(MemberRequestDto memberRequestDto) {
        Member existMemberEmail = memberRepository.findByMemberEmail(memberRequestDto.getMemberEmail());
        Member existMemberNickname = memberRepository.findByMemberNickname(memberRequestDto.getMemberNickname());

        if (existMemberEmail != null) {
            throw new BusinessExceptionHandler("이메일 중복을 확인해주세요", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
        if (existMemberNickname != null) {
            throw new BusinessExceptionHandler("닉네임 중복을 확인해주세요", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        memberRequestDto.setMemberStatus("T");
        memberRequestDto.setMemberPassword(passwordEncoder.encode(memberRequestDto.getMemberPassword()));

        Member member = Member.builder()
                .memberEmail(memberRequestDto.getMemberEmail())
                .memberPassword(memberRequestDto.getMemberPassword())
                .memberNickname(memberRequestDto.getMemberNickname())
                .memberPoint(memberRequestDto.getMemberPoint())
                .memberAddress(memberRequestDto.getMemberAddress())
                .memberDetailAddress(memberRequestDto.getMemberDetailAddress())
                .memberStatus(memberRequestDto.getMemberStatus())
                .build();

        memberRepository.save(member);
        return 1;
    }

    public int logout() {
        String currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findByMemberEmail(currentMemberId);
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByMemberId(member.getMemberId());

        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshTokenDto = refreshTokenOptional.get();
            refreshTokenRepository.deleteById(refreshTokenDto.getRefreshTokenId());
        }

        return 1;
    }

    public int withdrawal() {
        String currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findByMemberEmail(currentMemberId);
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByMemberId(member.getMemberId());

        if (refreshTokenOptional.isEmpty()) {
            throw new BusinessExceptionHandler("로그인 정보가 존재하지 않습니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        } else {
            RefreshToken refreshTokenDto = refreshTokenOptional.get();
            refreshTokenRepository.deleteById(refreshTokenDto.getRefreshTokenId());
            member.setMemberStatus("F");
            memberRepository.save(member);
            return 1;
        }
    }

    public int updateProfile(MemberRequestDto memberRequestDto) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();

        Member existingMember = memberRepository.findByMemberEmail(currentMemberId);

        if (existingMember == null) {
            throw new BusinessExceptionHandler("기존 회원 정보를 찾을 수 없습니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        Member existMemberEmail = memberRepository.findByMemberEmail(memberRequestDto.getMemberEmail());
        Member existMemberNickname = memberRepository.findByMemberNickname(memberRequestDto.getMemberNickname());

        if (existMemberEmail != null) {
            throw new BusinessExceptionHandler("이메일 중복을 확인해주세요", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        if (existMemberNickname != null) {
            throw new BusinessExceptionHandler("닉네임 중복을 확인해주세요", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

        existingMember.updateProfile(memberRequestDto);
        memberRepository.save(existingMember);

        return 1;
    }

    // 한 명 정보
    public MemberResponseDto findMember() {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberEmail(currentMemberId);

        if (member != null) {
            return MemberResponseDto.builder()
                    .memberId(member.getMemberId())
                    .memberEmail(member.getMemberEmail())
                    .memberPassword(member.getMemberPassword())
                    .memberNickname(member.getMemberNickname())
                    .memberAddress(member.getMemberAddress())
                    .memberDetailAddress(member.getMemberDetailAddress())
                    .memberPoint(member.getMemberPoint())
                    .memberStatus(member.getMemberStatus())
                    .build();
        } else {
            throw new BusinessExceptionHandler("회원 정보가 존재하지 않습니다.", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
    }
}
