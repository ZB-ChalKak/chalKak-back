package com.btb.chalKak.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteCommentRequest {
    
    private Long commentId;
}
