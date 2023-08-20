package com.btb.chalKak.domain.post.dto;

import com.btb.chalKak.domain.member.dto.MemberDto;
import com.btb.chalKak.domain.post.type.PostStatus;
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
    private Long viewCount;
    private Long likeCount;
    private PostStatus status;
    private MemberDto writer;

}
