package com.btb.chalKak.domain.comment.dto.response;

import com.btb.chalKak.domain.comment.entity.Comment;
import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.member.dto.WriterPreview;
import com.btb.chalKak.domain.post.dto.response.LoadPublicPostPreviewResponse;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class LoadPageCommentsResponse {

    private int currentPage;
    private int totalPages;

    private long totalElements;

    private List<CommentLoadResponse> commentLoadResponses;

    public static LoadPageCommentsResponse fromPage(Page<Comment> page) {
        List<CommentLoadResponse> commentLoadResponseList =
                page.getContent()
                        .stream()
                        .map(LoadPageCommentsResponse::fromEntity)
                        .collect(Collectors.toList());

        return LoadPageCommentsResponse.builder()
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalElements(page.getTotalElements())
                .commentLoadResponses(commentLoadResponseList)
                .build();
    }

    private static CommentLoadResponse fromEntity(Comment comment){

        return CommentLoadResponse.builder()
            .commentId(comment.getId())
            .comment(comment.getComment())
                .memberId(comment.getMember().getId())
            .profileUrl(comment.getMember().getProfileImg())
            .nickname(comment.getMember().getNickname())
            .createAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .build();

    }

}
