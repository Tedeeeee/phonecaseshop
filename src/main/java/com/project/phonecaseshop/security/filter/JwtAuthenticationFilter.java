package com.project.phonecaseshop.security.filter;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.RefreshToken;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.RefreshTokenRepository;
import com.project.phonecaseshop.security.utils.TokenUtil;
import com.project.phonecaseshop.utils.PasswordUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final List<Pattern> EXCLUDE_PATTERNS = Collections.unmodifiableList(Arrays.asList(
            Pattern.compile("/login"),
            Pattern.compile("/members/signup"),
            Pattern.compile("/products"),
            Pattern.compile("/products/\\d+"),
            Pattern.compile("/reviews/list/\\d+"),

            Pattern.compile("/members/findMembers"),
            Pattern.compile("/members/getRefreshToken/\\d+"),
            Pattern.compile("/etc/model"),
            Pattern.compile("/etc/photo"),
            Pattern.compile("/etc/design")
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String refreshToken = tokenUtil.extractRefreshToken(request)
                .filter(tokenUtil::isValidToken)
                .orElse(null);

        // refreshToken 이 있다는 것은 accessToken 이 만료되어서 보낸다는 것
        if (refreshToken != null) {
            // accessToken 재전송, refreshToken 또한 변경하여 전달
            reIssueAccessTokenAndRefreshToken(response, refreshToken);
        }

        // 없다는 것은 accessToken 이 유효 하다는 의미
        if (refreshToken == null) {
            // accessToken 이 유효해도 보안을 위해 새롭게 작성하여 전달.
            // 그리고 가장 중요한 토큰 해석 시작
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    public void reIssueAccessTokenAndRefreshToken(HttpServletResponse response, String refreshToken) throws IOException {
        Optional<RefreshToken> refreshTokenCheck = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (refreshTokenCheck.isPresent()) {
            Optional<Member> member = memberRepository.findById(refreshTokenCheck.get().getMemberId());
            if (member.isPresent()) {
                String reIssuedRefreshToken = reIssuedRefreshToken(refreshTokenCheck.get().getMemberId());
                String reIssuedAccessToken = tokenUtil.createAccessToken(member.get().getMemberEmail());
                tokenUtil.sendAccessAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
            }
        }
    }

    public String reIssuedRefreshToken(int memberId) {
        String reIssuedRefreshToken = tokenUtil.createRefreshToken();
        tokenUtil.insertRefreshToken(memberId, reIssuedRefreshToken);
        return reIssuedRefreshToken;
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            Optional<String> tokenOption = tokenUtil.extractAccessToken(request);

            String token = tokenOption.orElseThrow(() -> new RuntimeException("토큰이 존재하지 않습니다.1"));
            if (tokenUtil.isValidToken(token)) {

                String emailOption = tokenUtil.getMemberEmailFromToken(token);
                if (emailOption == null) {
                    throw new RuntimeException("유저의 정보가 맞지 않습니다");
                } else {
                    Member member = memberRepository.findByMemberEmail(emailOption);
                    if (member != null) {
                        saveAuthentication(member);
                        filterChain.doFilter(request, response);
                    }
                }
            } else {
                throw new RuntimeException("토큰이 유효하지 않습니다");
            }
        } catch (Exception e) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = jsonResponseWrapper(e);
            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }
    }

    public void saveAuthentication(Member member) {
        String password = member.getMemberPassword();
        if (password == null) {
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsMember = User.builder()
                .username(member.getMemberEmail())
                .password(password)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsMember, null, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private JSONObject jsonResponseWrapper(Exception e) {
        String resultMsg = "";

        if (e instanceof ExpiredJwtException) {
            resultMsg = "토큰이 만료되었습니다";
        } else if (e instanceof SignatureException) {
            resultMsg = "허용된 토큰이 아닙니다";
        } else if (e instanceof JwtException) {
            resultMsg = "토큰에 오류가 발생하였습니다";
        } else {
            resultMsg = "알수없는 오류가 발생하였습니다";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "9999");
        jsonMap.put("message", resultMsg);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        logger.error(resultMsg, e);
        return jsonObject;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();

        return EXCLUDE_PATTERNS.stream().anyMatch(pattern -> pattern.matcher(servletPath).matches());
    }
}
