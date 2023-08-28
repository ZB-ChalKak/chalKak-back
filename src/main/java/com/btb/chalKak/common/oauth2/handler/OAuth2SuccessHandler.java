package com.btb.chalKak.common.oauth2.handler;

import com.btb.chalKak.common.oauth2.service.TemporaryTokenStoreService;
import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.type.SuccessCode;
import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.service.Impl.TokenServiceImpl;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
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

  private final TokenServiceImpl tokenService;
  private final MemberRepository memberRepository;

//  private final ResponseService responseService;
  private final ObjectMapper objectMapper;
  private final TemporaryTokenStoreService temporaryTokenStoreService;


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

    TokenDto tokenDto = tokenService.createToken(email,member.getId());

    SignInMemberResponse data = SignInMemberResponse.builder()
        .userId(member.getId())
        .token(tokenDto)
        .build();

    String loginJsonMessage = objectMapper.writeValueAsString(
        CommonResponse.builder()
            .success(true)
            .message(SuccessCode.SUCCESS.getMessage())
            .data(data)
            .build()
    );

    response.setStatus(HttpStatus.OK.value());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(loginJsonMessage);

//    temporaryTokenStoreService.store(email, tokenDto);

    log.info(tokenDto.toString());

//    response.sendRedirect("/fetch-token"+"/"+ email);

  }
}