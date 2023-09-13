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

    private Boolean followed;

    public static LikerResponse fromEntity(Member member){

        return LikerResponse.builder()
                .nickName(member.getNickname())
                .memberId(member.getId())
                .profileUrl(member.getProfileImg())
                .build();

    }

    public void updateFollowed(boolean containsValue) {
        this.followed = containsValue;
    }
}
