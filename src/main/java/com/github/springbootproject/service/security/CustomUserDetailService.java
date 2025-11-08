package com.github.springbootproject.service.security;

import com.github.springbootproject.repository.roles.Roles;
import com.github.springbootproject.repository.userDetails.CustomUserDetails;
import com.github.springbootproject.repository.userPrincipal.UserPrincipal;
import com.github.springbootproject.repository.userPrincipal.UserPrincipalRoles;
import com.github.springbootproject.repository.userPrincipal.UserPrincipalRolesRepository;
import com.github.springbootproject.service.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Primary
public class CustomUserDetailService implements UserDetailsService {

    private final UserPrincipalRolesRepository userPrincipalRolesRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 이메일(아이디)로 유저 검색
        UserPrincipal userPrincipal = userPrincipalRolesRepository.findByEmailFetchJoin(email)
                .orElseThrow(() -> new NotFoundException("일치하는 유저가 없습니다. " + email));

        // 아이디, 이메일, 패스워드, 롤 정보를 반환
        return CustomUserDetails.builder()
                .userId(userPrincipal.getUser().getUserId())
                .email(userPrincipal.getEmail())
                .password(userPrincipal.getPassword())
                .authorities(userPrincipal.getUserPrincipalRoles().stream()
                        .map(UserPrincipalRoles::getRoles)
                        .map(Roles::getName)
                        .toList())
                .build();
    }
}
