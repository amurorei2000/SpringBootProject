package com.github.springbootproject.web.controller;

import com.github.springbootproject.service.AuthService;
import com.github.springbootproject.web.dto.auth.Login;
import com.github.springbootproject.web.dto.auth.SignUp;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/sign")
@RequiredArgsConstructor
public class SignController {

    private final AuthService authService;

    @PostMapping(value = "/register")
    public String register(@RequestBody SignUp signUpRequest) {
        boolean isSuccess = authService.signUp(signUpRequest);

        return isSuccess ? "회원 가입에 성공하였습니다." : "회원 가입에 실패하였습니다.";
    }

    @PostMapping(value = "/login")
    public String login(@RequestBody Login loginRequest, HttpServletResponse httpServletResponse) {
        String token = authService.login(loginRequest);
        httpServletResponse.setHeader("X-AUTH-TOKEN", token);
        return "로그인에 성공하였습니다.";
    }
}
