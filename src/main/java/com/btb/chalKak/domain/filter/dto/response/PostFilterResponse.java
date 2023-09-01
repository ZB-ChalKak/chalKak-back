package com.btb.chalKak.domain.filter.dto.response;

import com.btb.chalKak.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostFilterResponse {

    private Long postId;
    private String content;

    public static PostFilterResponse fromEntity(Post post){
        return PostFilterResponse.builder()
                .postId(post.getId())
                .content(post.getContent())
                .build();
    }
}
