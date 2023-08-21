package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.RefreshToken;
import com.project.phonecaseshop.entity.dto.MemberDto.MemberRequestDto;
import com.project.phonecaseshop.entity.dto.MemberDto.MemberResponseDto;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import com.project.phonecaseshop.responseApi.SingleResult;
import com.project.phonecaseshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ApiResponse apiResponse;

    // 회원가입
    @PostMapping("/signup")
    public CommonResult signUp(@RequestBody MemberRequestDto memberRequestDto) {
        System.out.println("회원가입");

        int signUpResult = memberService.signUp(memberRequestDto);

        if (signUpResult == 1) {
            return apiResponse.getSuccessResult(signUpResult);
        } else {
            return apiResponse.getFailResult("500", "회원 가입 실패: 중복 검사를 진행해주세요.");
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public CommonResult logout() {
        System.out.println("로그아웃");
        return apiResponse.getSuccessResult(memberService.logout());
    }

    // 회원정보 수정
    @PostMapping("/updateProfile")


    // ==========================================

    @GetMapping("/findMembers")
    public List<MemberResponseDto> findMembers() {
        return memberService.findMembers();
    }

    @GetMapping("/findMember/{id}")
    public MemberResponseDto findMember(@PathVariable Long id) {
        return memberService.findMember(id);
    }

    @GetMapping("/getRefreshToken/{id}")
    public RefreshToken findRefreshToken(@PathVariable Long id) {
        return memberService.findRefreshToken(id);
    }
}
