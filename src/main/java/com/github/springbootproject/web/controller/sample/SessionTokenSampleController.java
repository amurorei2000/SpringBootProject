package com.github.springbootproject.web.controller.sample;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Base64;

@RestController
@RequestMapping("/api")
public class SessionTokenSampleController {
    private final String secretKey = "123456789abcdefghijklmn";
    private final SecretKey secretHS256 = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));


    // 세션 생성하기
    @GetMapping("/set-session")
    public String setSession(HttpSession session) {
        session.setAttribute("user", "박원석");
        session.setAttribute("gender", "남자");
        session.setAttribute("job", "개발자");
        return "Session Set successfully!";
    }

    @GetMapping("/set-session2")
    public String setSession2(HttpSession session) {
        session.setAttribute("user", "선희");
        session.setAttribute("gender", "여자");
        session.setAttribute("job", "공인중개사");
        return "Session Set successfully!";
    }

    // 세션 정보 얻기
    @GetMapping("/get-session")
    public String getSession(HttpSession session) {
        String user = (String) session.getAttribute("user");
        String gender = (String) session.getAttribute("gender");
        String job = (String) session.getAttribute("job");

        return String.format("안녕하세요. 직업: %s, 성별: %s인 %s 입니다.", job, gender, user);
    }

    // Json Web Token 생성
    @GetMapping("/generate-token")
    public String generateToken(HttpServletResponse httpServletResponse) {


        String jwt = Jwts.builder()
                .setSubject("token1")
                .claim("user", "박원석")
                .claim("gender", "남자")
                .claim("job", "개발자")
                .signWith(secretHS256, SignatureAlgorithm.HS256)
                .compact();

        httpServletResponse.addHeader("Token", jwt);
        return "JWT set successfully!";
    }

    @GetMapping("/generate-token2")
    public String generateToken2(HttpServletResponse httpServletResponse) {


        String jwt = Jwts.builder()
                .setSubject("token2")
                .claim("user", "선희")
                .claim("gender", "여자")
                .claim("job", "공인중개사")
                .signWith(secretHS256, SignatureAlgorithm.HS256)
                .compact();

        httpServletResponse.addHeader("Token", jwt);
        return "JWT set successfully!";
    }

    // 토큰 검증 및 파싱
    @GetMapping("/validate-token")
    public String validateToken(@RequestHeader("Token") String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretHS256)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String user = (String) claims.get("user");
        String gender = (String) claims.get("gender");
        String job = (String) claims.get("job");

        return String.format("안녕하세요. 직업: %s, 성별: %s인 %s 입니다.", job, gender, user);
    }
}
