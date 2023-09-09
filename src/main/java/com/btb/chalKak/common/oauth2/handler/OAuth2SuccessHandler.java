package com.btb.chalKak.common.oauth2.handler;

import com.btb.chalKak.common.security.dto.TokenReissueResponse;
import com.btb.chalKak.common.security.entity.RefreshToken;
import com.btb.chalKak.common.security.jwt.JwtProvider;
import com.btb.chalKak.common.security.repository.RefreshTokenRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
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

    TokenReissueResponse token = jwtProvider.createToken(member.getEmail(), member.getRole());
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