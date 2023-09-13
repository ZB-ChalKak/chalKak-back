package com.btb.chalKak.domain.comment.dto.response;

import com.btb.chalKak.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentLoadResponse {

    private Long commentId;
    private String comment;
    private Long memberId;
    private String nickname;
    private String profileUrl;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;


}
