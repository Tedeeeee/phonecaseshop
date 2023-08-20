package com.project.phonecaseshop.security.service;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberEmail(username);

        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 회원입니다");
        }

        return User.builder()
                .username(member.getMemberEmail())
                .password(member.getMemberPassword())
                .build();
    }
}
