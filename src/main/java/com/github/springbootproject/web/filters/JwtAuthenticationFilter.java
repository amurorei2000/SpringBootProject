package com.github.springbootproject.web.filters;

import com.github.springbootproject.config.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 사용자 요청 헤더에서 JWT 토큰 정보 가져오기
        String jwtToken = jwtTokenProvider.resolveToken(request);

        // 토큰 예외 처리
        if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken)) {

            // 스프링 시큐리티 컨텍스트에 authetication(인증 정보)을 등록해서 사용자의 신원을 확인
            Authentication auth = jwtTokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 필터 체인에 등록
        filterChain.doFilter(request, response);
    }
}
