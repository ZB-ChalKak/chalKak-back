package com.btb.chalKak.domain.comment.controller;

import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_DELETE_COMMENT;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_COMMENT;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_MODIFY_COMMENT;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_SAVE_COMMENT;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.comment.dto.request.CreateCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.DeleteCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.ModifyCommentRequest;
import com.btb.chalKak.domain.comment.dto.response.CommentLoadResponse;
import com.btb.chalKak.domain.comment.dto.response.CommentResponse;
import com.btb.chalKak.domain.comment.dto.response.LoadPageCommentsResponse;
import com.btb.chalKak.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import com.btb.chalKak.domain.comment.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ResponseService responseService;

    @PostMapping("/comments")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createComment(
                            Authentication authentication,
                            @RequestBody CreateCommentRequest request) {
        CommentResponse data = CommentResponse.builder()
                .commentId(commentService.createComment(authentication, request).getId())
                .build();

        CommonResponse<?> response = responseService.success(data, SUCCESS_SAVE_COMMENT);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> loadComments(
        @PathVariable("postId") Long postId) {

        List<CommentLoadResponse> commentLoadResponses = commentService.loadComments(postId);

        CommonResponse<?> response = responseService.success(commentLoadResponses, SUCCESS_LOAD_COMMENT);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comments")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> modifyComment(
        Authentication authentication,
        @RequestBody ModifyCommentRequest request) {
        CommentResponse data = CommentResponse.builder()
            .commentId(commentService.modifyComment(authentication, request).getId())
            .build();

        CommonResponse<?> response = responseService.success(data, SUCCESS_MODIFY_COMMENT);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteComments(
        Authentication authentication,
        @RequestBody DeleteCommentRequest request) {

        boolean isDeleted = commentService.deleteComment(
                                authentication,
                                request);


        CommonResponse<?> response = responseService.success(isDeleted, SUCCESS_DELETE_COMMENT);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/pageComments")
    public ResponseEntity<?> loadCommentsOrderByDesc(
        @PathVariable Long postId, Pageable pageable)
    {

        Page<Comment> comments = commentService.loadCommentsOrderByDesc(postId, pageable);
        LoadPageCommentsResponse data = LoadPageCommentsResponse.fromPage(comments);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_COMMENT));
    }
}
