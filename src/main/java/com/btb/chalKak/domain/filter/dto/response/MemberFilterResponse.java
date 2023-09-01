package com.btb.chalKak.domain.filter.dto.response;

import com.btb.chalKak.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberFilterResponse {

    private Long memberId;
    private String nickname;
    private String profileImgUrl; // TODO

    public static MemberFilterResponse fromEntity(Member member){
        return MemberFilterResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImgUrl(member.getProfileImg())
                .build();
    }
}
