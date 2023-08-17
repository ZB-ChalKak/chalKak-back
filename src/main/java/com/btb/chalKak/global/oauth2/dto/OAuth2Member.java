package com.btb.chalKak.global.oauth2.dto;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class OAuth2Member implements OAuth2User, Serializable {

    private String registrationId;
    private Map<String, Object> attributes;
    private List<GrantedAuthority> authorities;
    private String email;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return this.registrationId;
    }

    public String getEmail() {
        return email;
    }
}