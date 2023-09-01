package com.btb.chalKak.domain.follow.dto.response;

import com.btb.chalKak.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowerResponse {

    private String nickName;
    private String profileUrl;

    public static FollowerResponse fromEntity(Member member){

        return FollowerResponse.builder()
                .nickName(member.getNickname())
                .profileUrl(member.getProfileImg())
                .build();

    }

}
