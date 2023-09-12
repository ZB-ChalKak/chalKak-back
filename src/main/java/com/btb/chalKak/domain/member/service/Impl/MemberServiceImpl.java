package com.btb.chalKak.domain.member.service.Impl;

import static com.btb.chalKak.common.exception.type.ErrorCode.ALREADY_EXISTS_EMAIL;
import static com.btb.chalKak.common.exception.type.ErrorCode.ALREADY_EXISTS_NICKNAME;
import static com.btb.chalKak.common.exception.type.ErrorCode.BLOCKED_MEMBER;
import static com.btb.chalKak.common.exception.type.ErrorCode.FORBIDDEN_RESPONSE;
import static com.btb.chalKak.common.exception.type.ErrorCode.INACTIVE_MEMBER;
import static com.btb.chalKak.common.exception.type.ErrorCode.INACTIVE_SING_IN;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_EMAIL;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_EMAIL_AUTH_TOKEN;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_EMAIL_LOGIN;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_MEMBER_ID;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_NICKNAME;
import static com.btb.chalKak.common.exception.type.ErrorCode.MALFORMED_JWT_EXCEPTION;
import static com.btb.chalKak.common.exception.type.ErrorCode.MISMATCH_PASSWORD;
import static com.btb.chalKak.common.exception.type.ErrorCode.UNSUPPORTED_JWT_EXCEPTION;
import static com.btb.chalKak.common.exception.type.ErrorCode.WITHDRAWAL_MEMBER;
import static com.btb.chalKak.domain.member.type.MemberProvider.CHALKAK;
import static com.btb.chalKak.domain.member.type.MemberStatus.ACTIVE;
import static com.btb.chalKak.domain.member.type.MemberStatus.BLOCKED;
import static com.btb.chalKak.domain.member.type.MemberStatus.INACTIVE;
import static com.btb.chalKak.domain.member.type.MemberStatus.WITHDRAWAL;

import com.btb.chalKak.common.exception.JwtException;
import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.common.security.customUser.CustomUserDetails;
import com.btb.chalKak.common.security.customUser.CustomUserDetailsService;
import com.btb.chalKak.common.security.dto.TokenReissueResponse;
import com.btb.chalKak.common.security.entity.RefreshToken;
import com.btb.chalKak.common.security.jwt.JwtProvider;
import com.btb.chalKak.common.security.repository.RefreshTokenRepository;
import com.btb.chalKak.common.security.request.TokenReissueRequest;
import com.btb.chalKak.common.smtp.MailComponents;
import com.btb.chalKak.domain.emailAuth.entity.EmailAuth;
import com.btb.chalKak.domain.emailAuth.repository.EmailAuthRepository;
import com.btb.chalKak.domain.follow.repository.FollowRepository;
import com.btb.chalKak.domain.member.dto.request.CheckPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.ModifyPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.ModifyUserInfoRequest;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.CheckPasswordResponse;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.dto.response.UserDetailsInfoResponse;
import com.btb.chalKak.domain.member.dto.response.UserInfoResponse;
import com.btb.chalKak.domain.member.dto.response.ValidateInfoResponse;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.member.service.MemberService;
import com.btb.chalKak.domain.member.type.MemberStatus;
import com.btb.chalKak.domain.photo.service.PhotoService;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final PhotoService photoService;
    private final EmailAuthRepository emailAuthRepository;
    private final MailComponents mailComponents;

    @Override
    @Transactional
    public void signUp(SignUpMemberRequest request) {
        // 1. 유효성 검사(이메일 및 닉네임 중복 확인)
        validateEmailDuplication(request.getEmail());
        validateNicknameDuplication(request.getNickname());

        // 2. 스타일 태그 조회
        List<StyleTag> styleTags = styleTagRepository.findAllById(request.getStyleTags());

        // 3. 멤버 저장
        Member member = memberRepository.save(Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .profileImg(null)
                .gender(request.getGender())
                .height(request.getHeight())
                .weight(request.getWeight())
                .provider(CHALKAK)
                .styleTags(styleTags)
                .status(INACTIVE)     // TODO : 이메일 인증 구현 완료 후에 수정 -> INACTIVE
                .build()
        );

        // TODO 회원 가입의 문제가 있을 시 로직을 Controller에서 따로 호출하도록 분리
        EmailAuth emailAuth = emailAuthRepository.save(EmailAuth.generateEmailAuth(member));
        sendConfirmEmail(member, emailAuth);
    }

    @Override
    @Transactional
    public SignInMemberResponse SignIn(SignInMemberRequest request) {

        // 1. 존재하는 이메일인지 확인
        Member member = getMemberByEmail(request.getEmail());
        Long memberId = member.getId();

        // 2. 멤버의 상태 확인
        if(member.getProvider() != CHALKAK){
            throw new MemberException(INVALID_EMAIL_LOGIN);
        }

        validateMemberStatus(member.getStatus());
      
        // 3. 비밀번호 일치 여부 확인
        if(!validatePasswordWithDB(request.getPassword(), member.getPassword())){
            throw new MemberException(MISMATCH_PASSWORD);
        }

        // 4. 토큰 생성
        TokenReissueResponse token = jwtProvider.createToken(member.getEmail(), member.getRole());
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

        // 6. response return (일반적으론 TokenDto return)
        return SignInMemberResponse.builder()
                .userInfo(UserInfoResponse.fromEntity(member))
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public TokenReissueResponse reissue(TokenReissueRequest tokenReissueRequest) {

        String refreshToken = tokenReissueRequest.getRefreshToken();
        //String accessToken = tokenRequestDto.getAccessToken();

        // 1. 만료된 refresh 토큰은 에러 발생
        if(!jwtProvider.validateToken(refreshToken)){
            throw new MemberException(INACTIVE_SING_IN);
        }

        /*
        // 2. accessToken에서 name 가져오기
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // 3. user pk로 유저 검색
        Member member = getMemberByEmail(authentication.getName());
         */

        // 2. userId로 member 가져오기
        Member member = memberRepository.findById(tokenReissueRequest.getUserId())
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));

        // 3. refresh token repository에 해당 userId의 refresh token이 있는지 검사
        RefreshToken refreshTokenStoredInDB = refreshTokenRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new JwtException(UNSUPPORTED_JWT_EXCEPTION));
        
        // 4. refresh 토큰 일치 검사
        if(!refreshTokenStoredInDB.getToken().equals(tokenReissueRequest.getRefreshToken())){
            throw new JwtException(MALFORMED_JWT_EXCEPTION);
        }

        TokenReissueResponse newTokenResponse = jwtProvider.createToken(member.getEmail(), member.getRole());
        refreshTokenRepository.save(refreshTokenStoredInDB.updateToken(newTokenResponse.getRefreshToken()));

        return newTokenResponse;
    }

    @Override
    @Transactional
    public void signOut(Authentication authentication) {

        // 1. authentication에서 member 추출
        Member member = getMemberByAuthentication(authentication);

        // 2. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 3. userId에 해당하는 user의 refreshToken을 확인하고, 있다면 삭제
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
                .nickname(member.getNickname())
                .postsCount(postRepository.countPostIdsByMemberId(member.getId()))
                .followingCount(getFollowingCountByUserId(userId))
                .followerCount(getFollowerCountByUserId(userId))
                .profileImg(member.getProfileImg())
                .build();
    }

    @Override
    @Transactional
    public UserInfoResponse userInfo(Authentication authentication, Long userId) {

        // 1. 유효한 이용자 id인지 검사
        validateMemberId(userId);

        // 2. authentication에서 member 추출
        Member member = getMemberByAuthentication(authentication);

        // 3. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 4. 토큰의 id와 전달된 id가 같은지 검사
        if(userId != member.getId()) {
            throw new MemberException(FORBIDDEN_RESPONSE);
        }

        // 5. 1에서 id를 검증했기 떄문에 굳이 elseThrow 할 필요 없긴 하다. 추후 리팩토링
        return UserInfoResponse.fromEntity(
                memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID)));
    }

    @Override
    @Transactional
    public CheckPasswordResponse checkPassword(Authentication authentication, CheckPasswordRequest passwordRequest) {

        // 1. authentication에서 member 추출
        Member member = getMemberByAuthentication(authentication);

        // 2. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 3. 유효한 이용자 id인지 검사
        validateMemberId(passwordRequest.getUserId());

        // 4. 토큰의 id와 전달된 id가 같은지 검사
        if(passwordRequest.getUserId() != member.getId()) {
            throw new MemberException(FORBIDDEN_RESPONSE);
        }

        // 5. 비밀번호 일치 여부 검사
        return CheckPasswordResponse.builder()
                .isPasswordMatch(validatePasswordWithDB(passwordRequest.getPassword(), member.getPassword()))
                .build();
    }

    @Override
    @Transactional
    public void modifyUserInfo(Authentication authentication,
                               Long userId,
                               MultipartFile[] multipartFiles,
                               ModifyUserInfoRequest infoRequest) {

        // 1. authentication에서 member 추출
        Member member = getMemberByAuthentication(authentication);

        // 2. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 3. 닉네임 중복 검사 -> 현재 닉네임과 같으면 검사 할 필요 없고, 같지 않다면 검사
        if(!member.getNickname().equals(infoRequest.getNickname())){
            validateNicknameDuplication(infoRequest.getNickname());
        }

        // 4. 수정 부
        List<StyleTag> styleTags = styleTagRepository.findAllById(infoRequest.getStyleTags());

        member.update(infoRequest.getNickname(),
                infoRequest.getGender(),
                infoRequest.getHeight(),
                infoRequest.getWeight(),
                styleTags,
                photoService.upload(multipartFiles));

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void withdrawUser(Authentication authentication) {

        // 1. authentication에서 member 추출
        Member member = getMemberByAuthentication(authentication);

        // 2. member의 status 검증
        validateMemberStatus(member.getStatus());

        // 3. 회원을 WITHDRAWAL 상태로 변경
        memberRepository.save(member.updateStatus(WITHDRAWAL));
    }

    @Override
    public Page<Post> loadPublicPosts(Authentication authentication, Long memberId, PageRequest pageRequest) {
        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));

        // 2. 회원이 작성한 게시물 조회
        List<Post> posts = postRepository.findAllByWriter(member);
        long totalCount = posts.size();

        return new PageImpl<>(posts, pageRequest, totalCount);
    }

    @Override
    @Transactional
    public void modifyPassword(Authentication authentication, ModifyPasswordRequest passwordRequest) {

        // 1. authentication에서 member 추출
        Member member = getMemberByAuthentication(authentication);

        // 2. member의 status 검증
        validateMemberStatus(member.getStatus());
        
        // 3. password 변경
        memberRepository.save(member.updatePassword(
                passwordEncoder.encode(passwordRequest.getPassword())));
    }

    @Override
    public void sendConfirmEmail(Member member, EmailAuth emailAuth) {
        String urlPrefix = "https://chal-kak.vercel.app/emailAuthPage";
//        String urlPrefix = "http://localhost:3000/emailAuthPage";
        String title = "#찰칵 인증 안내";
        String text = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                " <div" +
                "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"
                +
                "    <h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400; color: #000;\">"
                +
                "      <span style=\"font-size: 15px; margin: 0 0 10px 3px;\">#찰칵</span><br />" +
                "      <span style=\"color: #02b875\">이메일 인증</span> 안내입니다." +
                "    </h1>\n" +
                "    <p style=\"font-size: 16px; line-height: 26px; margin-top: 30px; padding: 0 5px; color: #000;\">"
                +
                member.getNickname() +
                "        님 안녕하세요.<br />" +
                "		아래 <b style=\"color: #02b875\">'메일 인증'</b> 버튼을 클릭하여 회원가입을 완료해 주세요.<br />" +
                "		감사합니다." +
                "	</p>" +
                "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\"" +
                "	href=\"" + urlPrefix + "/" + emailAuth.getId() + "?authToken=" + emailAuth.getEmailAuthToken() + "\" target=\"_blank\">" +
                "		<p" +
                "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">"
                +
                "			메일 인증</p>" +
                "	</a>" +
                "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" +
                " </div>"
                + "</body>"
                + "</html>";

        mailComponents.sendMail(member.getEmail(), title, text);
    }

    @Override
    public void confirmAuth(Long id, String authToken) {
        EmailAuth emailAuth = getEmailAuthById(id);
        validateEmailAuthTokenMatch(emailAuth, authToken);

        emailAuthRepository.save(emailAuth.confirmAuth());

        Member member = emailAuth.getMember();
        member.updateStatus(ACTIVE);
        memberRepository.save(member);
    }

    private EmailAuth getEmailAuthById(Long id) {
        return emailAuthRepository.findById(id)
                .orElseThrow(() -> new MemberException(INVALID_EMAIL));
    }

    private void validateEmailAuthTokenMatch(EmailAuth emailAuth, String authToken) {
        if (!emailAuth.getEmailAuthToken().equals(authToken)) {
            throw new MemberException(INVALID_EMAIL_AUTH_TOKEN);
        }
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

    private boolean validatePasswordWithDB(String inputPassword, String encodedPassword){
        return passwordEncoder.matches(inputPassword, encodedPassword);
    }

    private void validateMemberStatus(MemberStatus status){
        if(status == INACTIVE) {
            throw new MemberException(INACTIVE_MEMBER);
        }
        if(status == BLOCKED){
            throw new MemberException(BLOCKED_MEMBER);
        }
        if(status == WITHDRAWAL){
            throw new MemberException(WITHDRAWAL_MEMBER);
        }
    }

    public Member getMemberByAuthentication(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getMember();
    }

    public boolean validateMemberId(Authentication authentication, Long memberId){
        Member member = getMemberByAuthentication(authentication);
        if (member == null) {
            return false;
        }
        return Objects.equals(member.getId(), memberId);
    }

    private String getDecodingUrl(String urlString){
        try {
            return URLDecoder.decode(urlString, StandardCharsets.UTF_8);
        }catch(Exception e) {
            throw new MemberException(INVALID_NICKNAME);
        }
    }

    private Long getFollowingCountByUserId(Long userId){
        return followRepository.countByFollowerId(userId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));
    }

    private Long getFollowerCountByUserId(Long userId){
        return followRepository.countByFollowingId(userId)
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
