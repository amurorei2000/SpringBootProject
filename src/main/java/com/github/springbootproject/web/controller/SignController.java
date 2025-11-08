package com.github.springbootproject.web.controller;

import com.github.springbootproject.service.AuthService;
import com.github.springbootproject.web.dto.auth.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignController {

    private final AuthService authService;

    @PostMapping(value = "/register")
    public String register(@RequestBody SignUp signUpRequest) {
        boolean isSuccess = authService.signUp(signUpRequest);

        return isSuccess ? "회원 가입에 성공하였습니다." : "회원 가입에 실패하였습니다.";
    }
}
