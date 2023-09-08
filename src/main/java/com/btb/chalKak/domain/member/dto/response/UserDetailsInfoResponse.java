package com.btb.chalKak.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailsInfoResponse {

    private String nickname;
    private Long postsCount;
    private Long followerCount;
    private Long followingCount;
    private String profileImg;
}
