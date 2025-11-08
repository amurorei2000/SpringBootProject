package com.github.springbootproject.service;

import com.github.springbootproject.config.security.JwtTokenProvider;
import com.github.springbootproject.repository.roles.Roles;
import com.github.springbootproject.repository.roles.RolesRepository;
import com.github.springbootproject.repository.userPrincipal.UserPrincipal;
import com.github.springbootproject.repository.userPrincipal.UserPrincipalRepository;
import com.github.springbootproject.repository.userPrincipal.UserPrincipalRoles;
import com.github.springbootproject.repository.userPrincipal.UserPrincipalRolesRepository;
import com.github.springbootproject.repository.users.UserEntity;
import com.github.springbootproject.repository.users.UserJpaRepository;
import com.github.springbootproject.service.exceptions.NotAcceptException;
import com.github.springbootproject.service.exceptions.NotFoundException;
import com.github.springbootproject.web.dto.auth.Login;
import com.github.springbootproject.web.dto.auth.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserPrincipalRepository userPrincipalRepository;
    private final UserJpaRepository userJpaRepository;
    private final RolesRepository rolesRepository;
    private final UserPrincipalRolesRepository userPrincipalRolesRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(transactionManager = "transactionManager2")
    public boolean signUp(SignUp signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String userName = signUpRequest.getName();

        // 이미 등록된 Principal이 있는지 체크
        if (userPrincipalRepository.existsByEmail(email)) {
            return false;
        }

        // 같은 이름의 유저가 있으면 id만 등록하고, 없으면 새 유저를 DB에 생성
        UserEntity userFound = userJpaRepository.findByUserName(userName)
                .orElseGet(() -> userJpaRepository.save(UserEntity.builder()
                        .userName(userName)
                        .build())
                );

        // 유저 Principal 정보와 Role 정보를 DB에 등록
        Roles roles = rolesRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("ROLE_USERS를 찾을 수 없습니다."));

        UserPrincipal userPrincipal = UserPrincipal.builder()
                .email(email)
                .user(userFound)
                .password(passwordEncoder.encode(password))
                .build();

        userPrincipalRepository.save(userPrincipal);
        userPrincipalRolesRepository.save(UserPrincipalRoles.builder()
                .roles(roles)
                .userPrincipal(userPrincipal)
                .build());

        return true;
    }

    public String login(Login loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserPrincipal userPrincipal = userPrincipalRepository.findByEmailFetchJoin(email)
                    .orElseThrow(() -> new NotFoundException("User Principal을 찾을 수 없습니다."));

            List<String> roles = userPrincipal.getUserPrincipalRoles().stream()
                    .map(UserPrincipalRoles::getRoles)
                    .map(Roles::getName)
                    .toList();

            return jwtTokenProvider.createToken(email, roles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotAcceptException("로그인 할 수 없습니다.");
        }
    }
}
