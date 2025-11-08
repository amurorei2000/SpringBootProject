package com.github.springbootproject.service;

import com.github.springbootproject.web.dto.auth.SignUp;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public boolean signUp(SignUp signUpRequest) {
        String email = signUpRequest.getUserEmail();
        String password = signUpRequest.getPassword();
        String name = signUpRequest.getName();


    }
}
