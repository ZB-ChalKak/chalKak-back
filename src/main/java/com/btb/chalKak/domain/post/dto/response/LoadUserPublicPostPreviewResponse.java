package com.btb.chalKak.domain.post.dto.response;

import com.btb.chalKak.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadUserPublicPostPreviewResponse {

    private Long id;
    private String thumbnail;

    public static LoadUserPublicPostPreviewResponse fromEntity(Post post) {
        return LoadUserPublicPostPreviewResponse.builder()
                .id(post.getId())
                .thumbnail(post.getThumbnail())
                .build();
    }

}
