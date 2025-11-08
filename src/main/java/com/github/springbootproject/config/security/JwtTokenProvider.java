package com.github.springbootproject.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String secretKey = Base64.getEncoder().encodeToString("super-coding".getBytes(StandardCharsets.UTF_8));

    // 토큰 유효 기간 (1시간)
    private long tokenValidMilliseconds = 1000L * 60 * 60;

    // 사용자 조회 서비스
    private final UserDetailsService userDetailsService;

    // 시크릿 생성
    public SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // jwt 토큰 생성하기
    public String createToken(String email, List<String> roles) {
        // 아이디와 롤 정보를 클레임에 추가
        Claims claims = Jwts.claims()
                .subject(email)
                .build();

        claims.put("roles", roles);

        // 토큰 유효 기간
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenValidMilliseconds);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }


    // 요청 헤더에서 토큰 정보를 가져오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }


    // jwt 토큰에서 토큰 유효성 검사하기
    public boolean validateToken(String jwtToken) {
        try {
            // 토큰에서 클레임 정보 가져오기
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();

            // 만료시간이 현재 시간보다 미래면 통과
            Date now = new Date();
            return claims.getExpiration().after(now);
        } catch (Exception e) {
            return false;
        }
    }


    // 토큰에서 인증 정보 가져오기
    public Authentication getAuthentication(String jwtToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    // 토큰에서 이메일 가져오기
    private String getUserEmail(String jwtToken) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }
}
