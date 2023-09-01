package com.btb.chalKak.domain.like.dto;

import com.btb.chalKak.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikerResponse {

    private Long memberId;
    private String nickName;
    private String profileUrl;

    public static LikerResponse fromEntity(Member member){

        return LikerResponse.builder()
                .nickName(member.getNickname())
                .profileUrl(member.getProfileImg())
                .build();

    }

}
