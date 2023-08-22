package com.btb.chalKak.domain.member.dto.response;

import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInMemberResponse {

    Long userId;
    TokenDto token;

    public static SignInMemberResponse fromEntity(Member member){
        return SignInMemberResponse.builder()
                .userId(member.getId())
                .build();
    }
}
