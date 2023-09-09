package com.btb.chalKak.domain.member.dto.response;

import com.btb.chalKak.common.security.dto.TokenReissueResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInMemberResponse {

    private UserInfoResponse userInfo;
    private TokenReissueResponse token;
}
