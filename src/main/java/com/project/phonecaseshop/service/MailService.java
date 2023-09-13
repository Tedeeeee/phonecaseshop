package com.project.phonecaseshop.service;

import com.project.phonecaseshop.config.exception.BusinessExceptionHandler;
import com.project.phonecaseshop.config.exception.ErrorCode;
import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.dto.mailDto.MailDto;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.utils.PasswordUtil;
import com.project.phonecaseshop.utils.SecurityUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String myEmail;

    @Transactional
    public int sendEmail(String memberEmail) throws MessagingException {
        Member member = memberRepository.findByMemberEmail(memberEmail);
        if (member != null) {
            String s = PasswordUtil.generateRandomPassword();
            MailDto dto = new MailDto();
            dto.setAddress(memberEmail);
            dto.setTitle("핸드폰 케이스 샵의 임시 비밀번호 입니다.");
            dto.setMessage("안녕하세요. 핸드폰 케이스 샵 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                    + s + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
            dto.setMemberId(member.getMemberId());
            dto.setChangePassword(s);
            mailSend(dto);
            return 1;
        } else {
            throw new BusinessExceptionHandler("현재 사용자의 이메일과 일치하지 않습니다.", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
    }

    private void mailSend(MailDto mailDto) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);

        messageHelper.setTo(mailDto.getAddress());
        messageHelper.setSubject(mailDto.getTitle());
        messageHelper.setText(mailDto.getMessage());
        messageHelper.setFrom(myEmail);
        javaMailSender.send(mimeMessage);
        updatePassword(mailDto.getMemberId(), mailDto.getChangePassword());
    }

    private void updatePassword(int memberId, String password) {
        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isPresent()) {
            Member updateMember = Member.builder()
                    .memberId(memberId)
                    .memberEmail(member.get().getMemberEmail())
                    .memberPassword(passwordEncoder.encode(password))
                    .memberNickname(member.get().getMemberNickname())
                    .memberAddress(member.get().getMemberAddress())
                    .memberDetailAddress(member.get().getMemberDetailAddress())
                    .memberPoint(member.get().getMemberPoint())
                    .memberStatus(member.get().getMemberStatus())
                    .build();

            memberRepository.save(updateMember);
        } else {
            throw new BusinessExceptionHandler("사용자가 존재하지 않습니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
    }

}
