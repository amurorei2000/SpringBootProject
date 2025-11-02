package com.github.springbootproject.config;

import com.github.springbootproject.web.Interceptors.RequestTimeLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RequestTimeLoggingInterceptor requestTimeLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인터셉터 레지스트리에 커스텀 인터셉터를 추가하기
        registry.addInterceptor(requestTimeLoggingInterceptor);
    }
}
