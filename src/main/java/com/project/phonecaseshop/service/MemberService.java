package com.project.phonecaseshop.service;

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

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public String signUp(MemberRequestDto memberRequestDto) {
        Member existMemberEmail = memberRepository.findByMemberEmail(memberRequestDto.getMemberEmail());
        Member existMemberNickname = memberRepository.findByMemberNickname(memberRequestDto.getMemberNickname());

        if (existMemberEmail != null) {
            return "이메일 중복을 확인 해주세요";
        }
        if (existMemberNickname != null) {
            return "닉네임 중복을 확인 해주세요";
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
        return "성공하였습니다";
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

    public String withdrawal() {
        String currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findByMemberEmail(currentMemberId);
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByMemberId(member.getMemberId());

        if (refreshTokenOptional.isEmpty()) {
            return "실패했습니다";
        } else {
            RefreshToken refreshTokenDto = refreshTokenOptional.get();
            refreshTokenRepository.deleteById(refreshTokenDto.getRefreshTokenId());
            member.setMemberStatus("F");
            memberRepository.save(member);
            return "성공했습니다";
        }
    }

    public String updateProfile(MemberRequestDto memberRequestDto) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();

        Member existingMember = memberRepository.findByMemberEmail(currentMemberId);

        if (existingMember == null) {
            return "기존 회원 정보를 찾을 수 없습니다";
        }

        Member existMemberEmail = memberRepository.findByMemberEmail(memberRequestDto.getMemberEmail());
        Member existMemberNickname = memberRepository.findByMemberNickname(memberRequestDto.getMemberNickname());

        if (existMemberEmail != null) {
            return "이메일 중복을 확인 해주세요";
        }

        if (existMemberNickname != null) {
            return "닉네임 중복을 확인 해주세요";
        }

        existingMember.updateProfile(memberRequestDto);
        memberRepository.save(existingMember);

        return "성공했습니다";
    }

    // 한 명 정보
    public MemberResponseDto findMember() {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        System.out.println(currentMemberId);
        Member member = memberRepository.findByMemberEmail(currentMemberId);
        // 추후 예외처리 필요
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
    }

    // =============================================================

    // 다수 정보
    public List<MemberResponseDto> findMembers() {
        List<Member> all = memberRepository.findAll();

        return all.stream()
                .map(member -> MemberResponseDto.builder()
                        .memberId(member.getMemberId())
                        .memberEmail(member.getMemberEmail())
                        .memberPassword(member.getMemberPassword())
                        .memberNickname(member.getMemberNickname())
                        .memberPoint(member.getMemberPoint())
                        .memberAddress(member.getMemberAddress())
                        .memberDetailAddress(member.getMemberDetailAddress())
                        .memberStatus(member.getMemberStatus())
                        .build())
                .toList();
    }

    // 리프레쉬 토큰
    public RefreshToken findRefreshToken(int id) {
        return refreshTokenRepository.findByMemberId(id).orElse(null);
    }

}
