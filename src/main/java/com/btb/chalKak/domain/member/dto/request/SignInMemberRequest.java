package com.btb.chalKak.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInMemberRequest {

    private String email;
    private String password;
}
