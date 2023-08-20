package com.project.phonecaseshop.service;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.dto.MemberDto.MemberRequestDto;
import com.project.phonecaseshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberService{

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

        memberRepository.save(member);
    }

    public List<Member> findMember() {
        return memberRepository.findAll();
    }
}
