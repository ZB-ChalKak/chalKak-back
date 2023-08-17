package com.btb.chalKak.global.security;

import static com.btb.chalKak.domain.member.type.MemberStatus.ACTIVE;
import static com.btb.chalKak.domain.member.type.MemberStatus.BLOCKED;
import static com.btb.chalKak.domain.member.type.MemberStatus.WITHDRAWAL;

import com.btb.chalKak.domain.member.entity.Member;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(member.getRole().getRole()));
        return auth;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 사용자 계정 만료(탈퇴) 여부 반환
        return member.getStatus() != WITHDRAWAL;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 사용자 계정 잠금 여부 반환
        return member.getStatus() != BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 사용자 인증 정보 만료 여부 반환
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 사용자 계정 활성화 여부 반환
        return member.getStatus() == ACTIVE;
    }

}
