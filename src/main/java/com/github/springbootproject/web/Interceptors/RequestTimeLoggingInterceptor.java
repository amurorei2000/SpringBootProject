package com.github.springbootproject.web.Interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class RequestTimeLoggingInterceptor implements HandlerInterceptor {

    // request 요청 전 실행
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();

        // Attribute에 시작 시간 등록
        request.setAttribute("requestStartTime", startTime);
        return true;
    }

    // request 요청 후 실행
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // request에 등록된 Attribute에서 시작 시간 가져오기
        long startTime = (long) request.getAttribute("requestStartTime");
        long excuteTime = System.currentTimeMillis() - startTime;

        log.info("{} {} executed in {} ms", request.getMethod(), request.getRequestURI(), excuteTime);
    }
}
