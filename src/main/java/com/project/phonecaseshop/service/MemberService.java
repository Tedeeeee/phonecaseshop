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


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public void signUp(MemberRequestDto memberRequestDto) {
        Member memberFind = memberRepository.findByMemberEmail(memberRequestDto.getMemberEmail());
        if (memberFind != null) {
            throw new RuntimeException("이미 존재하는 회원입니다");
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
    }

    public void logout() {
        String currentMemberId = SecurityUtil.getCurrentMemberId();

    }

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

}
