package com.project.phonecaseshop.service;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.dto.MemberDto.MemberRequestDto;
import com.project.phonecaseshop.entity.dto.MemberDto.MemberResponseDto;
import com.project.phonecaseshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signUp(MemberRequestDto memberRequestDto) {
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

    public void updateRefreshToken(Long memberId, String memberRefreshToken) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("존재하지않는 회원입니다"));

        System.out.println(member.toString());


    }

    public List<MemberResponseDto> findMembers() {
        List<Member> all = memberRepository.findAll();

        return all.stream()
                .map(member -> MemberResponseDto.builder()
                        .memberId(member.getMemberId())
                        .memberEmail(member.getMemberEmail())
                        .memberNickname(member.getMemberNickname())
                        .memberPoint(member.getMemberPoint())
                        .memberAddress(member.getMemberAddress())
                        .memberDetailAddress(member.getMemberDetailAddress())
                        .build())
                .toList();
    }

    public List<MemberResponseDto> findMember() {
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
                        .build())
                .toList();
    }
    public Member findMember(String memberEmail) {
        return memberRepository.findByMemberEmail(memberEmail);
    }
}
