package com.project.phonecaseshop.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.phonecaseshop.entity.RefreshToken;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.*;

@Component
@Log4j2
@RequiredArgsConstructor
public class TokenUtil {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";

    public String createAccessToken(String memberEmail) {
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(memberEmail))
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setExpiration(createAccessTokenExpiredDate())
                .signWith(createSignature(), SignatureAlgorithm.HS256);
        return builder.compact();
    }

    public String createRefreshToken() {
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(createRefreshTokenExpiredDate())
                .signWith(createSignature(), SignatureAlgorithm.HS256);
        return builder.compact();
    }

    // 캘린더를 사용해서 작성하는 만료 시간
    // accessToken 만료기간
    private static Date createAccessTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 6);
        return c.getTime();
    }

    // refreshToken 만료기간
    private static Date createRefreshTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7);
        return c.getTime();
    }

    // header 설정
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    // memberEmail 넣기
    private static Map<String, String> createClaims(String memberEmail) {
        Map<String, String> claims = new HashMap<>();

        claims.put("memberEmail", memberEmail);
        return claims;
    }

    // 내가 보유한 시크릿키 서명 알고리즘 작성
    private Key createSignature() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Authentication 이라고 적힌 값을 accessToken 으로 인지하고 리턴
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    // AuthenticationRefresh 라고 적힌 값을 refreshToken 으로 인지하고 리턴
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    // 토큰 안에 있는 정보 가져오기
    private Claims getClaimsFormToken(String token) {
        Key key = createSignature();
        try {
            Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            log.info("토큰 해석 성공: " + body);
            return body;
        } catch (JwtException e) {
            log.error("토큰 해석 실패" + e.getMessage());
            return null;
        }
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            log.info("expireTime : " + claims.getExpiration());

            return true;
        } catch (ExpiredJwtException exception) {
            log.error("만료된 JWT 토큰입니다");
            return false;
        } catch (JwtException exception) {
            log.error("토큰에 문제가 있습니다.");
            return false;
        } catch (NullPointerException exception) {
            log.error("토큰이 존재하지 않습니다");
            return false;
        }
    }

    // memberEmail 알아오는 메소드
    public String getMemberEmailFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.get("memberEmail").toString();
    }


    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, String> data = new HashMap<>();
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(data);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonData);

        log.info("AccessToken, RefreshToken 데이터 전송 완료");
    }
    public void insertRefreshToken(int memberId, String refreshTokenValue) {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .memberId(memberId)
                    .refreshToken(refreshTokenValue)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
    }
}
