package com.project.phonecaseshop.service;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.RefreshToken;
import com.project.phonecaseshop.entity.dto.MemberDto.MemberRequestDto;
import com.project.phonecaseshop.entity.dto.MemberDto.MemberResponseDto;
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


    public int signUp(MemberRequestDto memberRequestDto) {
        int i;
        Member memberFind = memberRepository.findByMemberEmail(memberRequestDto.getMemberEmail());
        if (memberFind != null) {
            return 0;
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

        System.out.println(member.toString());
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


    // =============================================================
    public List<MemberResponseDto> findMembers() {
        List<Member> all = memberRepository.findAll();

//        for (Member value : all) {
//            RefreshToken refreshToken = refreshTokenRepository.findByMemberId(value.getMemberId());
//            if (refreshToken != null) {
//                System.out.println("refreshToken = " + refreshToken.toString());
//            }
//            System.out.println(value.toString());
//        }

        return all.stream()
                .map(member -> MemberResponseDto.builder()
                        .memberId(member.getMemberId())
                        .memberEmail(member.getMemberEmail())
                        .memberPassword(member.getMemberPassword())
                        .memberNickname(member.getMemberNickname())
                        .memberPoint(member.getMemberPoint())
                        .memberAddress(member.getMemberAddress())
                        .memberDetailAddress(member.getMemberDetailAddress())
                        .build())
                .toList();
    }
    public MemberResponseDto findMember(Long id) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        System.out.println(currentMemberId);
        Member member = memberRepository.findById(id).orElse(null);
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .memberEmail(member.getMemberEmail())
                .memberPassword(member.getMemberPassword())
                .memberNickname(member.getMemberNickname())
                .memberAddress(member.getMemberAddress())
                .memberDetailAddress(member.getMemberDetailAddress())
                .memberPoint(member.getMemberPoint())
                .build();
    }

    public RefreshToken findRefreshToken(Long id) {
        return refreshTokenRepository.findByMemberId(id).orElse(null);

    }

}
