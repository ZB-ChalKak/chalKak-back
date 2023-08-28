package com.btb.chalKak.common.security.service;

import com.btb.chalKak.common.security.dto.TokenDto;

public interface TokenService {


  TokenDto createToken(String email, Long memberId);
}
