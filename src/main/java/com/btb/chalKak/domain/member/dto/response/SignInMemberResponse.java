package com.btb.chalKak.domain.member.dto.response;

import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SignInMemberResponse {

    private UserInfoResponse userInfo;
    private TokenDto token;
}
