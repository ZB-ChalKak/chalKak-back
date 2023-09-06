package com.btb.chalKak.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckPasswordResponse {

    private boolean isPasswordMatch;
}
