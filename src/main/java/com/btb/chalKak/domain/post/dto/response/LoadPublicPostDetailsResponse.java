package com.btb.chalKak.domain.post.dto.response;

import com.btb.chalKak.domain.member.dto.Writer;
import com.btb.chalKak.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadPublicPostDetailsResponse {

    private Long id;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private Writer writer;

    public static LoadPublicPostDetailsResponse fromEntity(Post post) {
        return LoadPublicPostDetailsResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .writer(Writer.fromEntity(post.getWriter()))
                .build();
    }
}
