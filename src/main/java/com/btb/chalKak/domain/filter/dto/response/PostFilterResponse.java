package com.btb.chalKak.domain.filter.dto.response;

import com.btb.chalKak.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostFilterResponse {

    private Long postId;
    private String content;
    private String previewContent;

    public static PostFilterResponse fromEntity(Post post){
        return PostFilterResponse.builder()
                .postId(post.getId())
                .content(post.getContent())
                .build();
    }

    public PostFilterResponse setPreviewContent(String previewContent){
        this.previewContent = previewContent;
        return this;
    }
}
