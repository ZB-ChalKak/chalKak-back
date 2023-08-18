package com.btb.chalKak.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCommentRequest {
    
    private Long memberId;
    private Long postId;
    private String content;

}
