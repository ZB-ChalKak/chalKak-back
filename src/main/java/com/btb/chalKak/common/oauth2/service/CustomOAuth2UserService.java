package com.btb.chalKak.common.oauth2.service;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.common.oauth2.dto.OAuthAttributes;
import com.btb.chalKak.common.oauth2.dto.SessionMember;
import java.util.Collections;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.btb.chalKak.domain.member.type.MemberStatus.ACTIVE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
                            throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);  // OAuth2 정보를 가져옵니다.

        Map<String, Object> originAttributes = oAuth2User.getAttributes();  // OAuth2User의 attribute

        // OAuth2 서비스 id (google)
        String registrationId = userRequest
                                .getClientRegistration()
                                .getRegistrationId();    // 소셜 정보를 가져옵니다.
        log.info("getAccessToken " + userRequest.getAccessToken().getTokenValue());

        String userNameAttributeName = userRequest
                                    .getClientRegistration()
                                    .getProviderDetails()
                                    .getUserInfoEndpoint()
                                    .getUserNameAttributeName();


        // OAuthAttributes: OAuth2User의 attribute를 서비스 유형에 맞게 담아줄 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName ,oAuth2User.getAttributes());

        // 1. member가 없을 경우

        Member member =  createGoolgeMember(attributes);


        // 3. member가 있고, 구글 소셜 로그인인 경우

        if(member.getStatus() != ACTIVE){
            throw new RuntimeException("비활성 계정입니다.");
        }

        member = UpdateGoogleMember(member);
        httpSession.setAttribute("member",new SessionMember(member));
//        String email = member.getEmail();
//        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(email);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole().getRole()))
                                                , attributes.getAttributes()
                                                ,attributes.getNameAttributesKey());

    }

    private Member UpdateGoogleMember(Member member) {

        log.info("oAuth 유저 수정 처리 진행");
        member.update(member.getNickname(),member.getProfileImg());

        return memberRepository.save(member);
    }

    private Member createGoolgeMember(OAuthAttributes authAttributes) {

        log.info("oAuth 유저 등록 처리 진행");
        Member member = memberRepository.findByEmail(authAttributes.getEmail())
                .orElse(null);

//        if(member != null && member.getProvider() != GOOGLE){
//            log.info("구글 계정이 아닙니다.");
//            throw new RuntimeException("구글 계정이 아닙니다.");
//        }

        if(member == null){
            member = authAttributes.toEntity();
            memberRepository.save(member);
        }

        return member;
    }
}
