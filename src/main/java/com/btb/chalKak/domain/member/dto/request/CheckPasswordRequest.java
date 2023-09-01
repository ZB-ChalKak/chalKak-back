package com.btb.chalKak.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckPasswordRequest {

    private Long userId;
    private String password;
}
