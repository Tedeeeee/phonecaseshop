package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.RefreshToken;
import com.project.phonecaseshop.entity.dto.memberDto.MemberRequestDto;
import com.project.phonecaseshop.entity.dto.memberDto.MemberResponseDto;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
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

        String signUpResult = memberService.signUp(memberRequestDto);

        if (signUpResult.equals("성공하였습니다")) {
            return apiResponse.getSuccessResult(1);
        } else {
            return apiResponse.getFailResult("500", signUpResult);
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public CommonResult logout() {
        System.out.println("로그아웃");
        return apiResponse.getSuccessResult(memberService.logout());
    }


    // 회원탈퇴
    @GetMapping("/withdrawal")
    public CommonResult withdrawal() {
        System.out.println("회원탈퇴");

        String result = memberService.withdrawal();
        System.out.println(result);

        if (result.equals("성공했습니다")) {
            return apiResponse.getSuccessResult(1);
        } else {
            return apiResponse.getFailResult("500", result);
        }
    }

    // 회원정보 수정
    @PutMapping("")
    public CommonResult updateProfile(@RequestBody MemberRequestDto memberRequestDto) {
        System.out.println("정보 수정");

        String result = memberService.updateProfile(memberRequestDto);

        if (result.equals("성공했습니다")) {
            return apiResponse.getSuccessResult(1);
        } else {
            return apiResponse.getFailResult("500", result);
        }
    }

    // 회원 정보 가져오기
    @GetMapping("")
    public MemberResponseDto findMember() {
        return memberService.findMember();
    }


    // ==========================================

    @GetMapping("/findMembers")
    public List<MemberResponseDto> findMembers() {
        return memberService.findMembers();
    }

    @GetMapping("/getRefreshToken/{id}")
    public RefreshToken findRefreshToken(@PathVariable int id) {
        return memberService.findRefreshToken(id);
    }
}
