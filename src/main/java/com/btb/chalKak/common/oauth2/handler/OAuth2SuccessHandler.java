package com.btb.chalKak.common.oauth2.handler;

import com.btb.chalKak.common.exception.type.SuccessCode;
import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.entity.RefreshToken;
import com.btb.chalKak.common.security.jwt.JwtProvider;
import com.btb.chalKak.common.security.repository.RefreshTokenRepository;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.dto.response.UserInfoResponse;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

//  private final TokenServiceImpl tokenService;
  private final MemberRepository memberRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  private final JwtProvider jwtProvider;

//  private final ResponseService responseService;
  private final ObjectMapper objectMapper;


  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    log.info("OAuth2 Login 성공!");

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = oAuth2User.getAttribute("email");  // Google의 경우 "email" 키를 사용합니다.
//    Long memberId = memberService.findMemberId(email);
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(()->new RuntimeException("NOT_FOUND_MEMBER"));
    // 이메일 주소를 로깅 또는 다른 로직에 사용
    log.info("User email: " + email);
    log.info("User Id : " + member.getId());

    TokenDto token = jwtProvider.createToken(member.getEmail(), member.getRole());
    String refreshToken = token.getRefreshToken();

    // 4. refresh 토큰 저장
    RefreshToken tokenStoredInDB =
        refreshTokenRepository.findByMemberId(member.getId()).orElse(null);

    if (tokenStoredInDB != null) {
      refreshTokenRepository.save(tokenStoredInDB.updateToken(refreshToken));
    } else {
      refreshTokenRepository.save(
          RefreshToken.builder()
              .memberId(member.getId())
              .token(refreshToken)
              .build());
    }

    SignInMemberResponse data = SignInMemberResponse.builder()
            .userInfo(UserInfoResponse.fromEntity(member))
            .token(token)
            .build();

    String loginJsonMessage = objectMapper.writeValueAsString(
        CommonResponse.builder()
            .success(true)
            .message(SuccessCode.SUCCESS.getMessage())
            .data(data)
            .build()
    );

    Cookie tokenCookie = new Cookie("accessToken", token.getAccessToken());
    Cookie userCookie = new Cookie("userId", member.getId().toString());

    tokenCookie.setSecure(true);  // Send cookie over HTTPS only
    tokenCookie.setHttpOnly(true);  // Make cookie accessible only through the HTTP protocol
    tokenCookie.setPath("/");  // Setting path
//    tokenCookie.setMaxAge(24 * 60 * 60);  // Set expiry date after 24 Hrs

    userCookie.setHttpOnly(true);
    userCookie.setPath("/");
//    userCookie.setMaxAge(24 * 60 * 60);

    response.addCookie(tokenCookie);
    response.addCookie(userCookie);

    response.setStatus(HttpStatus.OK.value());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");
    response.sendRedirect("https://chal-kak.vercel.app/userinfo/modify-userinfo?userId="
        +member.getId() +
        "&accessToken=" + token.getAccessToken() +
        "&refreshToken=" + token.getRefreshToken() +
        "&accessTokenExpireDate=" + token.getAccessTokenExpireDate()
    );


//    temporaryTokenStoreService.store(email, tokenDto);

    log.info(token.toString());

//    response.sendRedirect("/fetch-token"+"/"+ email);

  }
}