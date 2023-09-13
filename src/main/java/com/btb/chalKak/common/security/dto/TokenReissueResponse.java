package com.btb.chalKak.common.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenReissueResponse {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;

    @Override
    public String toString() {
        return "TokenDto{" +
            "grantType='" + grantType + '\'' +
            ", accessToken='" + accessToken + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            ", accessTokenExpireDate=" + accessTokenExpireDate +
            '}';
    }
}
