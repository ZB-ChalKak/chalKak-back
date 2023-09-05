package com.btb.chalKak.domain.member.dto.response;

import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SignInMemberResponse {

    private Long userId;
    private List<Long> styleTags;
    private TokenDto token;

    public static SignInMemberResponse fromEntity(Member member){
        return SignInMemberResponse.builder()
                .userId(member.getId())
                .build();
    }
}
