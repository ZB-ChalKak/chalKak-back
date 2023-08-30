package com.btb.chalKak.domain.post.dto.response;

import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.member.dto.WriterPreview;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadPublicPostPreviewResponse {

    private Long id;
    private String content;
    private String location;

    private Long viewCount;
    private Long likeCount;

    private List<String> styleTags;
    private List<String> hashTags;

    private WriterPreview writer;

    public static LoadPublicPostPreviewResponse fromEntity(Post post) {
        return LoadPublicPostPreviewResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .location(post.getLocation())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .styleTags(post.getStyleTags().stream()
                        .map(StyleTag::getKeyword)
                        .collect(Collectors.toList()))
                .hashTags(post.getHashTags().stream()
                        .map(HashTag::getKeyword)
                        .collect(Collectors.toList()))
                .writer(WriterPreview.fromEntity(post.getWriter()))
                .build();
    }

}
