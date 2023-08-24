package com.btb.chalKak.domain.member.service.Impl;

import static com.btb.chalKak.common.response.type.ErrorCode.ALREADY_EXISTS_EMAIL;
import static com.btb.chalKak.common.response.type.ErrorCode.ALREADY_EXISTS_NICKNAME;

import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.common.security.JwtProvider;
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
import com.btb.chalKak.domain.member.type.MemberRole;
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
    private final JwtProvider jwtTokenProvider;
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
                .privacyHeight(request.isPrivacyHeight())
                .privacyWeight(request.isPrivacyWeight())
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
    public SignInMemberResponse logIn(SignInMemberRequest request) {

        // 1. 존재하는 이메일인지 확인
        Member member = getMemberByEmail(request.getEmail());

        // 2. 비밀번호 일치 여부 확인 -> passEncoder match 함수 사용
        checkPassword(request.getPassword(), member.getPassword());
        
        // 3. 토큰 생성
        TokenDto token = jwtTokenProvider.createToken(member.getEmail(), MemberRole.USER);

        // 4. refresh 토큰 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(member.getId())
                .refreshToken(token.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. response return (일반적으론 TokenDto return)
        return SignInMemberResponse.builder()
                .userId(member.getId())
                .token(token)
                .build();
    }

    // TODO : 코드 리뷰 후 리팩토링 요
    @Override
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. 만료된 refresh 토큰은 에러 발생
        if(!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("CustomRefreshTokenException");
        }

        // 2. accessToken에서 name 가져오기
        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // 3. user pk로 유저 검색
        Member member = memberRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("CustomUserNotFoundException"));

        // 4. repository에 refresh token이 있는지 검사
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new RuntimeException("CustomRefreshTokenException"));
        
        // 5. refresh 토큰 일치 검사
        if(!refreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("CustomRefreshTokenException");
        }

        TokenDto newToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
        RefreshToken newRefreshToken = refreshToken.updateToken(newToken.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return newToken;
    }

    private Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("CustomMemberException"));
    }

    private void checkPassword(String actual, String expected){
        if(!passwordEncoder.matches(actual, expected)) {
            throw new RuntimeException("CustomMemberException");
        }
    }
}
