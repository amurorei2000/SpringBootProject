package com.github.springbootproject.web.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    // 커스텀 필터 체인을 추가
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 필터 체인 들어가기 전 실행
        log.info(method + uri + " 요청이 들어왔습니다.");

        filterChain.doFilter(request, response);

        // 필터 체인에서 나갈 때 실행
        log.info(method + uri + "가 상태 " + response.getStatus() + " (으)로 응답이 나갑니다.");
    }
}
