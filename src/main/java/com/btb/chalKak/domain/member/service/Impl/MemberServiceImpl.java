package com.btb.chalKak.domain.member.service.Impl;

import static com.btb.chalKak.common.exception.type.ErrorCode.ALREADY_EXISTS_EMAIL;
import static com.btb.chalKak.common.exception.type.ErrorCode.ALREADY_EXISTS_NICKNAME;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_EMAIL;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_MEMBER_ID;
import static com.btb.chalKak.common.exception.type.ErrorCode.MISMATCH_PASSWORD;
import static com.btb.chalKak.domain.member.type.MemberProvider.CHALKAK;


import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.common.security.customUser.CustomUserDetails;
import com.btb.chalKak.common.security.jwt.JwtProvider;
import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.entity.RefreshToken;
import com.btb.chalKak.common.security.repository.RefreshTokenRepository;
import com.btb.chalKak.common.security.request.TokenRequestDto;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.member.service.MemberService;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final StyleTagRepository styleTagRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
        // 1. 만료된 refresh 토큰은 에러 발생
        if(!jwtProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("CustomRefreshTokenException ");
        }

        // 2. accessToken에서 name 가져오기
        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // 3. user pk로 유저 검색
        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("CustomUserNotFoundException"));

        // 4. repository에 refresh token이 있는지 검사
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new RuntimeException("CustomRefreshTokenException"));
        
        // 5. refresh 토큰 일치 검사
        if(!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("CustomRefreshTokenException");
        }

        TokenDto newToken = jwtProvider.createToken(member.getEmail(), member.getRole());
        RefreshToken newRefreshToken = refreshToken.updateToken(newToken.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return newToken;
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

    public Long findMemberId(String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("CustomMemberException"));

        return member.getId();
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

}
