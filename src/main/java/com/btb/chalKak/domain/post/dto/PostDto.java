package com.btb.chalKak.domain.post.dto;

import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.type.PostStatus;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String content;
    private Long hitCount;
    private Long likeCount;
    private PostStatus status;
    private Member writer;

    private List<StyleTag> styleTags;
    private List<HashTag> hashTags;
}
