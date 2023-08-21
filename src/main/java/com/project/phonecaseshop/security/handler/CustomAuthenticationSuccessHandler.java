package com.project.phonecaseshop.security.handler;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.security.utils.TokenUtil;
import com.project.phonecaseshop.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenUtil tokenUtil;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String memberEmail = extractUsername(authentication);
        String accessToken = tokenUtil.createAccessToken(memberEmail);
        String refreshToken = tokenUtil.createRefreshToken();

        Member member = memberRepository.findByMemberEmail(memberEmail);

        if (member != null) {
            if (member.getMemberStatus().equals("T")) {
                tokenUtil.insertRefreshToken(member.getMemberId(), refreshToken);
                tokenUtil.sendAccessAndRefreshToken(response, accessToken, refreshToken);
            } else {
                throw new RuntimeException("탈퇴한 회원입니다");
            }
        } else {
            throw new RuntimeException("존재하지 않는 회원입니다1.");
        }

        log.info("로그인에 성공하였습니다. 이메일 : {}", memberEmail);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("로그인에 성공하였습니다. RefreshToken : {}", refreshToken);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
