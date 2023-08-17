package com.btb.chalKak.global.oauth2.service;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.global.oauth2.type.OAuth2CustomUser;
import com.btb.chalKak.global.oauth2.type.OauthDto;
import com.btb.chalKak.global.oauth2.util.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository MemberRepository;
    private final CustomAuthorityUtils authorityUtils;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);  // OAuth2 정보를 가져옵니다.

        Map<String, Object> originAttributes = oAuth2User.getAttributes();  // OAuth2User의 attribute

        // OAuth2 서비스 id (google)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();    // 소셜 정보를 가져옵니다.

        // OAuthAttributes: OAuth2User의 attribute를 서비스 유형에 맞게 담아줄 클래스
        OauthDto attributes = OauthDto.of(registrationId, originAttributes);
        Member member = saveOrUpdate(attributes);
        String email = member.getEmail();
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(email);

        return new OAuth2CustomUser(registrationId, originAttributes, authorities, email);
    }

    private Member saveOrUpdate(OauthDto authAttributes) {
        Member member = MemberRepository.findByEmail(authAttributes.getEmail())
                .map(members -> members.update(authAttributes.getName(), authAttributes.getProfileImageUrl()))
                .orElse(authAttributes.toEntity());

        return MemberRepository.save(member);
    }
}
