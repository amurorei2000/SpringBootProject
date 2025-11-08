package com.github.springbootproject.web.controller.direction;

import com.github.springbootproject.service.exceptions.CAuthenticationEntryPointException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exceptions")
@RequiredArgsConstructor
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public void entrypointException() {
        throw new CAuthenticationEntryPointException("인증 과정에서 에러");
    }

    @GetMapping(value = "/access-denied")
    public void accessDeniedException() {
        throw new AccessDeniedException("");
    }
}
