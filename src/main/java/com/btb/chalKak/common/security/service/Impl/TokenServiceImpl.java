package com.btb.chalKak.common.security.service.Impl;

import com.btb.chalKak.common.security.JwtProvider;
import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.entity.RefreshToken;
import com.btb.chalKak.common.security.repository.RefreshTokenRepository;
import com.btb.chalKak.common.security.service.TokenService;
import com.btb.chalKak.domain.member.type.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {


  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtProvider jwtTokenProvider;

  @Override
  public TokenDto createToken(String email, Long memberId) {


    TokenDto token = jwtTokenProvider.createToken(email, MemberRole.USER);

    // 4. refresh 토큰 저장
    RefreshToken refreshToken = RefreshToken.builder()
        .memberId(memberId)
        .token(token.getRefreshToken())
        .build();

    refreshTokenRepository.save(refreshToken);

    return token;
  }
}
