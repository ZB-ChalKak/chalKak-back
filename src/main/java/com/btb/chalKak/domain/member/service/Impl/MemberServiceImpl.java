package com.btb.chalKak.domain.member.service.Impl;

import static com.btb.chalKak.common.exception.type.ErrorCode.*;
import static com.btb.chalKak.domain.member.type.MemberProvider.CHALKAK;


import com.btb.chalKak.common.exception.JwtException;
import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.common.security.customUser.CustomUserDetails;
import com.btb.chalKak.common.security.customUser.CustomUserDetailsService;
import com.btb.chalKak.common.security.jwt.JwtProvider;
import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.entity.RefreshToken;
import com.btb.chalKak.common.security.repository.RefreshTokenRepository;
import com.btb.chalKak.common.security.request.TokenRequestDto;
import com.btb.chalKak.domain.follow.repository.FollowRepository;
import com.btb.chalKak.domain.member.dto.request.CheckPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.dto.response.UserDetailsInfoResponse;
import com.btb.chalKak.domain.member.dto.response.UserInfoResponse;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.member.service.MemberService;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;

import java.net.URLDecoder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final StyleTagRepository styleTagRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final FollowRepository followRepository;

    @Override
    @Transactional
    public void signUp(SignUpMemberRequest request) {
        // 1. 유효성 검사(이메일 및 닉네임 중복 확인)
        validateEmailDuplication(request.getEmail());
        validateNicknameDuplication(request.getNickname());

        // 2. 스타일 태그 조회
        List<StyleTag> styleTags = styleTagRepository.findAllById(request.getStyleTags());

        // 3. 멤버 저장 TODO 프로필 이미지
        memberRepository.save(Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
//                .profileImg(request.getProfileImg())
                .gender(request.getGender())
                .height(request.getHeight())
                .weight(request.getWeight())
                .provider(CHALKAK)
                .styleTags(styleTags)
                .build()
        );
    }

    private void validateEmailDuplication(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberException(ALREADY_EXISTS_EMAIL);
        }
    }

    private void validateNicknameDuplication(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberException(ALREADY_EXISTS_NICKNAME);
        }
    }

    @Override
    @Transactional
    public SignInMemberResponse SignIn(SignInMemberRequest request) {
        // 1. 존재하는 이메일인지 확인
        Member member = getMemberByEmail(request.getEmail());
        Long memberId = member.getId();

        if(member.getProvider() != CHALKAK){
            new RuntimeException("CHALKAK 계정이 아닙니다.");
        }
      
        // 2. 비밀번호 일치 여부 확인
        validatePasswordWithDB(request.getPassword(), member.getPassword());

        // 3. 토큰 생성
        TokenDto token = jwtProvider.createToken(member.getEmail(), member.getRole());
        String refreshToken = token.getRefreshToken();

        // 4. refresh 토큰 저장
        RefreshToken tokenStoredInDB =
                refreshTokenRepository.findByMemberId(memberId).orElse(null);

        if (tokenStoredInDB != null) {
            refreshTokenRepository.save(tokenStoredInDB.updateToken(refreshToken));
        } else {
            refreshTokenRepository.save(
                    RefreshToken.builder()
                            .memberId(memberId)
                            .token(refreshToken)
                            .build());
        }

        // 5. response return (일반적으론 TokenDto return)
        return SignInMemberResponse.builder()
                .userId(memberId)
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        String refreshToken = tokenRequestDto.getRefreshToken();
        String accessToken = tokenRequestDto.getAccessToken();

        // 1. 만료된 refresh 토큰은 에러 발생
        validateToken(refreshToken);

        // 2. accessToken에서 name 가져오기
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // 3. user pk로 유저 검색
        Member member = getMemberByEmail(authentication.getName());

        // 4. repository에 refresh token이 있는지 검사
        RefreshToken refreshTokenStoredInDB = refreshTokenRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new RuntimeException("CustomRefreshTokenException"));
        
        // 5. refresh 토큰 일치 검사
        if(!refreshTokenStoredInDB.getToken().equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("CustomRefreshTokenException");
        }

        TokenDto newToken = jwtProvider.createToken(member.getEmail(), member.getRole());
        RefreshToken newRefreshToken = refreshTokenStoredInDB.updateToken(newToken.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return newToken;
    }
    
    // TODO : 리팩토링
    private void validateToken(String token) {
        if(!jwtProvider.validateToken(token)) {
            throw new JwtException(SIGNATURE_EXCEPTION);
        }
    }

    private Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(INVALID_EMAIL));
    }

    private void validatePasswordWithDB(String inputPassword, String encodedPassword){
        if(!passwordEncoder.matches(inputPassword, encodedPassword)) {
            throw new MemberException(MISMATCH_PASSWORD);
        }
    }

    public Member getMemberByAuthentication(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getMember();
    }

    public boolean validateMemberId (Authentication authentication, Long memberId){
        Member member = getMemberByAuthentication(authentication);

        if(!member.getId().equals(memberId)){
            throw new MemberException(INVALID_MEMBER_ID);
        }

        return true;
    }

    @Override
    @Transactional
    public void signOut(HttpServletRequest servletRequest) {
        // 1. 토큰 추출
        String accessToken = jwtProvider.resolveTokenFromRequest(servletRequest);

        // 2. accessToken 검증
        validateToken(accessToken);

        // 3. accessToken에서 memberId 가져오기
        String email = jwtProvider.getSubjectByToken(accessToken);
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
        Member member = customUserDetails.getMember();

        // 4. userId에 해당하는 user의 refreshToken을 확인하고, 있다면 삭제
        RefreshToken refreshTokenStoredInDB =
                refreshTokenRepository.findByMemberId(member.getId())
                        .orElse(null);

        if(refreshTokenStoredInDB != null){
            refreshTokenRepository.deleteByMemberId(member.getId());
        }
    }

    @Override
    @Transactional
    public void validateEmail(String email) {
        if(memberRepository.existsByEmail(email)){
            throw new MemberException(ALREADY_EXISTS_EMAIL);
        }
    }

    @Override
    @Transactional
    public void validateNickname(String nickname) {
        if(memberRepository.existsByNickname(getDecodingNicknameWithBase64(nickname))){
            throw new MemberException(ALREADY_EXISTS_NICKNAME);
        }
    }

    private String getDecodingNicknameWithBase64(String nickname){
        try {
            return URLDecoder.decode(nickname);
        }catch(Exception e) {
            throw new MemberException(INVALID_NICKNAME);
        }
    }

    @Override
    @Transactional
    public UserDetailsInfoResponse userDetailsInfo(Long userId) {
        return UserDetailsInfoResponse.builder()
                //.posts()
                .followingCount(getFollowingCountByUserId(userId))
                .followerCount(getFollowerCountByUserId(userId))
                .build();
    }

    private Long getFollowingCountByUserId(Long userId){
        return followRepository.countByFollowingId(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));
    }

    private Long getFollowerCountByUserId(Long userId){
        return followRepository.countByFollowerId(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));
    }

    @Override
    @Transactional
    public UserInfoResponse userInfo(HttpServletRequest request, Long userId) {
        // 1. 토큰 추출
        String accessToken = jwtProvider.resolveTokenFromRequest(request);

        // 2. accessToken 검증
        validateToken(accessToken);
        
        // 3. 토큰으로 이용자 추출
        Member member = getMemberByAccessToken(accessToken);

        // 4. 유효한 이용자 id인지 검사
        validateMemberId(userId);

        // 5. 토큰의 id와 전달된 id가 같은지 검사
        if(userId != member.getId()) {
            throw new MemberException(FORBIDDEN_RESPONSE);
        }

        return UserInfoResponse.fromEntity(
                memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID)));
    }

    private Member getMemberByAccessToken(String accessToken){
        String email = jwtProvider.getSubjectByToken(accessToken);
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
        return customUserDetails.getMember();
    }

    private void validateMemberId(Long userId){
        memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));
    }

    @Override
    public void checkPassword(HttpServletRequest servletRequest, CheckPasswordRequest passwordRequest) {
        // 1. 토큰 추출
        String accessToken = jwtProvider.resolveTokenFromRequest(servletRequest);

        // 2. accessToken 검증
        validateToken(accessToken);

        // 3. 토큰으로 이용자 추출
        Member member = getMemberByAccessToken(accessToken);

        // 4. 유효한 이용자 id인지 검사
        validateMemberId(passwordRequest.getUserId());

        // 5. 토큰의 id와 전달된 id가 같은지 검사
        if(passwordRequest.getUserId() != member.getId()) {
            throw new MemberException(FORBIDDEN_RESPONSE);
        }

        // 6. 비밀번호 일치 여부 검사
        validatePasswordWithDB(passwordRequest.getPassword(), member.getPassword());
    }
}
