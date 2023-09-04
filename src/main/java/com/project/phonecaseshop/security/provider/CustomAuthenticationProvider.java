package com.project.phonecaseshop.security.provider;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.RefreshToken;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.RefreshTokenRepository;
import com.project.phonecaseshop.security.service.CustomUserDetailService;
import com.project.phonecaseshop.security.service.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailService customUserDetailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetails user = customUserDetailService.loadUserByUsername(username);
        Member member = memberRepository.findByMemberEmail(user.getUsername());
        Optional<RefreshToken> byMemberId = refreshTokenRepository.findByMemberId(member.getMemberId());

        if (byMemberId.isPresent()) {
            throw new RuntimeException("이미 접속중인 사용자입니다");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }


        return new UsernamePasswordAuthenticationToken(user, null, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
