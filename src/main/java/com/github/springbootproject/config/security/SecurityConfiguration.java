package com.github.springbootproject.config.security;

import com.github.springbootproject.web.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    // 시큐리티에서 불필요한 옵션 끄기
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.frameOptions(
                        frameOptions -> frameOptions.sameOrigin())
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // http 기본 인증 끄기
                .httpBasic(AbstractHttpConfigurer::disable)
                // 접속 정보 저장 끄기
                .rememberMe(AbstractHttpConfigurer::disable)
                // 세션 끄기
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 인증 예외 처리
                .authorizeHttpRequests(
                        auth -> auth
                                // 인증 없이 사용 가능한 uri
                                .requestMatchers("/resources/static/**", "/v1/api/sign/*").permitAll()
                                .requestMatchers("/v1/api/air-reservation/*").hasRole("USER")
                                .anyRequest().authenticated()
                )
                // 인증 실패 처리
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomerAccessDeniedHandler())
                )
                // 필터 우선 적용 처리
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 인증 관리자 가져오기
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // CORS 허용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // CORS를 허용할 오리진 주소를 등록하기
        configuration.setAllowedOrigins(List.of("http://localhost:63342"));
        // Token을 주고 받는 것을 허용
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("X-AUTH-TOKEN");
        // 헤더 설정 허용
        configuration.addAllowedHeader("*");
        // API 메소드 허용
        configuration.addAllowedMethod("*");
        // CORS 허용 유지 시간 (1시간)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
