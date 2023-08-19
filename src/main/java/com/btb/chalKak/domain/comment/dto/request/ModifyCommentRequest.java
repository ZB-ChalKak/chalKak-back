package com.btb.chalKak.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyCommentRequest {
    
    private Long memberId;
    private Long commentId;
    private String content;

}
