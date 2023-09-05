package com.btb.chalKak.domain.member.service.Impl;

import static com.btb.chalKak.common.exception.type.ErrorCode.*;
import static com.btb.chalKak.common.exception.type.ErrorCode.ALREADY_EXISTS_EMAIL;
import static com.btb.chalKak.common.exception.type.ErrorCode.ALREADY_EXISTS_NICKNAME;
import static com.btb.chalKak.common.exception.type.ErrorCode.BLOCKED_MEMBER;
import static com.btb.chalKak.common.exception.type.ErrorCode.EXPIRED_JWT_EXCEPTION;
import static com.btb.chalKak.common.exception.type.ErrorCode.FORBIDDEN_RESPONSE;
import static com.btb.chalKak.common.exception.type.ErrorCode.INACTIVE_MEMBER;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_EMAIL;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_EMAIL_LOGIN;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_MEMBER_ID;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_NICKNAME;
import static com.btb.chalKak.common.exception.type.ErrorCode.MISMATCH_PASSWORD;
import static com.btb.chalKak.common.exception.type.ErrorCode.WITHDRAWAL_MEMBER;
import static com.btb.chalKak.domain.member.type.MemberProvider.CHALKAK;
import static com.btb.chalKak.domain.member.type.MemberStatus.ACTIVE;
import static com.btb.chalKak.domain.member.type.MemberStatus.BLOCKED;
import static com.btb.chalKak.domain.member.type.MemberStatus.WITHDRAWAL;

import com.btb.chalKak.common.exception.JwtException;
import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.common.security.customUser.CustomUserDetails;
import com.btb.chalKak.common.security.customUser.CustomUserDetailsService;
import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.entity.RefreshToken;
import com.btb.chalKak.common.security.jwt.JwtProvider;
import com.btb.chalKak.common.security.repository.RefreshTokenRepository;
import com.btb.chalKak.common.security.request.TokenRequestDto;
import com.btb.chalKak.domain.follow.repository.FollowRepository;
import com.btb.chalKak.domain.member.dto.request.CheckPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.ModifyUserInfoRequest;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.dto.response.UserDetailsInfoResponse;
import com.btb.chalKak.domain.member.dto.response.UserInfoResponse;
import com.btb.chalKak.domain.member.dto.response.ValidateInfoResponse;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.member.service.MemberService;
import com.btb.chalKak.domain.member.type.MemberStatus;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
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

    @Override
    @Transactional
    public SignInMemberResponse SignIn(SignInMemberRequest request) {
        // 1. 존재하는 이메일인지 확인
        Member member = getMemberByEmail(request.getEmail());
        Long memberId = member.getId();

        // 2. 멤버의 상태 확인
        if(member.getProvider() != CHALKAK){
            new MemberException(INVALID_EMAIL_LOGIN);
        }

        validateMemberStatus(member.getStatus());
      
        // 3. 비밀번호 일치 여부 확인
        validatePasswordWithDB(request.getPassword(), member.getPassword());

        // 4. 토큰 생성
        TokenDto token = jwtProvider.createToken(member.getEmail(), member.getRole());
        String refreshToken = token.getRefreshToken();

        // 5. refresh 토큰 저장
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

        // 6. 회원을 ACTIVE 상태로 변경
        // -> 현재는 회원 가입 시 default 값이 ACTIVE라 의미 없음. 이메일 인증 구현 시 수정.
        memberRepository.save(member.updateStatus(ACTIVE));

        // 7. response return (일반적으론 TokenDto return)
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
        if(!jwtProvider.validateToken(refreshToken)){
            throw new MemberException(INACTIVE_SING_IN);
        }

        // 2. accessToken에서 name 가져오기
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // 3. user pk로 유저 검색
        Member member = getMemberByEmail(authentication.getName());

        // 4. repository에 refresh token이 있는지 검사
        RefreshToken refreshTokenStoredInDB = refreshTokenRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new JwtException(UNSUPPORTED_JWT_EXCEPTION));
        
        // 5. refresh 토큰 일치 검사
        if(!refreshTokenStoredInDB.getToken().equals(tokenRequestDto.getRefreshToken())){
            throw new JwtException(MALFORMED_JWT_EXCEPTION);
        }

        TokenDto newToken = jwtProvider.createToken(member.getEmail(), member.getRole());
        RefreshToken newRefreshToken = refreshTokenStoredInDB.updateToken(newToken.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return newToken;
    }

    @Override
    @Transactional
    public void signOut(HttpServletRequest servletRequest) {
        // 1. 토큰 추출
        String accessToken = jwtProvider.resolveTokenFromRequest(servletRequest);

        // 2. accessToken 검증
        if(!jwtProvider.validateToken(accessToken)) {
            throw new JwtException(EXPIRED_JWT_EXCEPTION);
        }

        // 3. accessToken에서 memberId 추출
        Member member = getMemberByAccessToken(accessToken);

        // 4. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 5. userId에 해당하는 user의 refreshToken을 확인하고, 있다면 삭제
        RefreshToken refreshTokenStoredInDB =
                refreshTokenRepository.findByMemberId(member.getId())
                        .orElse(null);

        if(refreshTokenStoredInDB != null){
            refreshTokenRepository.deleteByMemberId(member.getId());
        }
    }

    @Override
    @Transactional
    public ValidateInfoResponse validateEmail(String email) {
        return ValidateInfoResponse.builder()
                .isDuplicated(memberRepository.existsByEmail(email))
                .build();
    }

    @Override
    @Transactional
    public ValidateInfoResponse validateNickname(String nickname) {
        return ValidateInfoResponse.builder()
                .isDuplicated(memberRepository.existsByNickname(getDecodingUrl(nickname)))
                .build();
    }

    @Override
    @Transactional
    public UserDetailsInfoResponse userDetailsInfo(Long userId) {
        // 1. 유효한 userId인지 검사
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));

        // 2. 추출된 member의 status 검증
        validateMemberStatus(member.getStatus());

        return UserDetailsInfoResponse.builder()
                .postsCount(postRepository.countPostIdsByMemberId(member.getId()))
                .followingCount(getFollowingCountByUserId(userId))
                .followerCount(getFollowerCountByUserId(userId))
                .build();
    }

    @Override
    @Transactional
    public UserInfoResponse userInfo(HttpServletRequest request, Long userId) {
        // 1. 유효한 이용자 id인지 검사
        validateMemberId(userId);

        // 2. 토큰 추출
        String accessToken = jwtProvider.resolveTokenFromRequest(request);

        // 3. accessToken 검증
        if(!jwtProvider.validateToken(accessToken)) {
            throw new JwtException(EXPIRED_JWT_EXCEPTION);
        }
        
        // 4. 토큰으로 이용자 추출
        Member member = getMemberByAccessToken(accessToken);

        // 5. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 6. 토큰의 id와 전달된 id가 같은지 검사
        if(userId != member.getId()) {
            throw new MemberException(FORBIDDEN_RESPONSE);
        }

        // 7. 1에서 id를 검증했기 떄문에 굳이 elseThrow 할 필요 없긴 하다. 추후 리팩토링
        return UserInfoResponse.fromEntity(
                memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID)));
    }

    @Override
    @Transactional
    public void checkPassword(HttpServletRequest servletRequest, CheckPasswordRequest passwordRequest) {
        // 1. 토큰 추출
        String accessToken = jwtProvider.resolveTokenFromRequest(servletRequest);

        // 2. accessToken 검증
        if(!jwtProvider.validateToken(accessToken)) {
            throw new JwtException(EXPIRED_JWT_EXCEPTION);
        }

        // 3. 토큰으로 이용자 추출
        Member member = getMemberByAccessToken(accessToken);

        // 4. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 5. 유효한 이용자 id인지 검사
        validateMemberId(passwordRequest.getUserId());

        // 6. 토큰의 id와 전달된 id가 같은지 검사
        if(passwordRequest.getUserId() != member.getId()) {
            throw new MemberException(FORBIDDEN_RESPONSE);
        }

        // 7. 비밀번호 일치 여부 검사
        validatePasswordWithDB(passwordRequest.getPassword(), member.getPassword());
    }

    @Override
    @Transactional
    public void modifyUserInfo(HttpServletRequest servletRequest, ModifyUserInfoRequest infoRequest) {
        // 1. 토큰 추출
        String accessToken = jwtProvider.resolveTokenFromRequest(servletRequest);

        // 2. accessToken 검증
        if(!jwtProvider.validateToken(accessToken)) {
            throw new JwtException(EXPIRED_JWT_EXCEPTION);
        }

        // 3. 토큰으로 이용자 추출
        Member member = getMemberByAccessToken(accessToken);

        // 4. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 5. 닉네임 중복 검사
        //validateNicknameDuplication(infoRequest.getNickname());

        // 6. 수정 부
        // TODO : profileImg 부분 수정 요
        List<StyleTag> styleTags = styleTagRepository.findAllById(infoRequest.getStyleTags());

        member.update(infoRequest.getNickname(),
                infoRequest.getGender(),
                infoRequest.getHeight(),
                infoRequest.getWeight(),
                styleTags,
                infoRequest.getProfileImg());   // TODO : profileImg 부분 수정 요
    }

    @Override
    @Transactional
    public void withdrawUser(HttpServletRequest request) {
        // 1. 토큰 추출
        String accessToken = jwtProvider.resolveTokenFromRequest(request);

        // 2. accessToken 검증
        if(!jwtProvider.validateToken(accessToken)) {
            throw new JwtException(EXPIRED_JWT_EXCEPTION);
        }

        // 3. 토큰으로 이용자 추출
        Member member = getMemberByAccessToken(accessToken);

        // 5. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 4. 회원을 WITHDRAWAL 상태로 변경
        memberRepository.save(member.updateStatus(WITHDRAWAL));
    }

    @Override
    public Page<Post> loadPublicPosts(Authentication authentication, int page, int size) {
        if (authentication == null) {
            return null;
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = customUserDetails.getMember();

        List<Post> posts = postRepository.findAllByWriter(member);

        return new PageImpl<>(posts, pageRequest, posts.size());
    }

    private void validateEmailDuplication(String email) {
        Member member = memberRepository.findByEmail(email).orElse(null);

        if(member != null && member.getStatus() != WITHDRAWAL){
            throw new MemberException(ALREADY_EXISTS_EMAIL);
        }
    }

    private void validateNicknameDuplication(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElse(null);

        if(member != null && member.getStatus() != WITHDRAWAL) {
            throw new MemberException(ALREADY_EXISTS_NICKNAME);
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

    private void validateMemberStatus(MemberStatus status){
        /*
        if(status == INACTIVE) {
            throw new MemberException(INACTIVE_MEMBER);
        }
        */
        if(status == BLOCKED){
            throw new MemberException(BLOCKED_MEMBER);
        }
        if(status == WITHDRAWAL){
            throw new MemberException(WITHDRAWAL_MEMBER);
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

    private String getDecodingUrl(String urlString){
        try {
            return URLDecoder.decode(urlString, "UTF-8");
        }catch(Exception e) {
            throw new MemberException(INVALID_NICKNAME);
        }
    }

    private Long getFollowingCountByUserId(Long userId){
        return followRepository.countByFollowingId(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));
    }

    private Long getFollowerCountByUserId(Long userId){
        return followRepository.countByFollowerId(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));
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

}
