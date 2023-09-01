package com.btb.chalKak.domain.member.dto.response;

import com.btb.chalKak.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserDetailsInfoResponse {

    private List<Post> posts;
    private Long followerCount;
    private Long followingCount;
}
