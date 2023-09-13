package com.btb.chalKak.common.security.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenReissueRequest {

    Long userId;
    String refreshToken;
}
