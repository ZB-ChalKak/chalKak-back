package com.btb.chalKak.common.oauth2.service;

import com.btb.chalKak.common.security.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TemporaryTokenStoreService {

  private final ConcurrentHashMap<String, TokenRecord> tokenStore = new ConcurrentHashMap<>();

  public void store(String email, TokenDto tokenDto) {
    TokenRecord tokenRecord = new TokenRecord(tokenDto, LocalDateTime.now().plus(10, ChronoUnit.SECONDS));
    tokenStore.put(email, tokenRecord);
  }

  public TokenDto retrieve(String email) {
    TokenRecord tokenRecord = tokenStore.get(email);
    log.info("record token : " + email);
    if (tokenRecord == null) {
      throw new RuntimeException("등록된 token이 없습니다.");
    }
    // Remove token if expired
    if (LocalDateTime.now().isAfter(tokenRecord.getExpiryTime())) {
      tokenStore.remove(email);
      throw new RuntimeException("token 유효 시간을 초과하였습니다.");
    }
    return tokenRecord.getTokenDto();
  }

  public void remove(String email) {
    tokenStore.remove(email);
  }

  private static class TokenRecord {
    private final TokenDto tokenDto;
    private final LocalDateTime expiryTime;

    public TokenRecord(TokenDto tokenDto, LocalDateTime expiryTime) {
      this.tokenDto = tokenDto;
      this.expiryTime = expiryTime;
    }

    public TokenDto getTokenDto() {
      return tokenDto;
    }

    public LocalDateTime getExpiryTime() {
      return expiryTime;
    }
  }
}
