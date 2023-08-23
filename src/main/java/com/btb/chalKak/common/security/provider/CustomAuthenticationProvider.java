package com.btb.chalKak.common.security.provider;

import com.btb.chalKak.common.security.service.Impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails member = userDetailsService.loadUserByUsername(email);

        // 1. 회원 유효성 검사
        validateAuthenticate(member);

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("이메일 또는 비밀번호를 잘못 입력했습니다.");
        }

        return new UsernamePasswordAuthenticationToken(email, password, member.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    private void validateAuthenticate(UserDetails member) {
        if (member == null) {
            throw new InternalAuthenticationServiceException("인증 서비스가 존재하지 않습니다.");
        }
        if (!member.isAccountNonExpired()) {
            throw new LockedException("사용자 계정이 탈퇴되었습니다.");
        }
        if (!member.isAccountNonLocked()) {
            throw new LockedException("사용자 계정이 정지되었습니다.");
        }
        if (!member.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("사용자 인증 정보가 만료되었습니다.");
        }
        if(!member.isEnabled()){
            throw new DisabledException("사용자 계정이 비활성화되었습니다.");
        }
    }
}
