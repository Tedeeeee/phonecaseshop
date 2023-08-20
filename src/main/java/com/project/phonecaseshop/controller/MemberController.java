package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.dto.MemberDto.MemberRequestDto;
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

    @GetMapping("/findMember")
    public List<Member> findMember() {
        return memberService.findMember();
    }
}
