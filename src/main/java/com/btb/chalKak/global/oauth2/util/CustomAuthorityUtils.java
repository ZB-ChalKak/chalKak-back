package com.btb.chalKak.global.oauth2.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthorityUtils {

    // 이 메서드는 사용자의 이메일을 기반으로 권한을 생성합니다.
    // 귀하의 특정 권한 요구 사항과 일치하도록 이 로직을 수정할 수 있습니다.
    public List<GrantedAuthority> createAuthorities(String email) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 예시: 기본 사용자 역할 할당
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 이메일을 다른 역할 및 권한에 매핑하기 위해 여기에 추가 로직을 추가하세요.
        // 예시: if (email.endsWith("@admin.com")) { authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN")); }

        return authorities;
    }
}