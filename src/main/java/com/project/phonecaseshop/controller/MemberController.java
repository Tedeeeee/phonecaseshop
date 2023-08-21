package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.dto.MemberDto.MemberRequestDto;
import com.project.phonecaseshop.entity.dto.MemberDto.MemberResponseDto;
import com.project.phonecaseshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public void signUp(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.signUp(memberRequestDto);
    }
    // 로그아웃
    @GetMapping("/logout")
    public void logout() {
        memberService.logout();
    }


    @GetMapping("/findMembers")
    public List<MemberResponseDto> findMembers() {
        return memberService.findMembers();
    }

    @GetMapping("/findMember/{id}")
    public MemberResponseDto findMember(@PathVariable Long id) {
        return memberService.findMember(id);
    }
}
