package com.btb.chalKak.domain.comment.controller;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.comment.dto.request.CreateCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.DeleteCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.ModifyCommentRequest;
import com.btb.chalKak.domain.comment.dto.response.CommentLoadResponse;
import com.btb.chalKak.domain.comment.dto.response.CommentResponse;
import com.btb.chalKak.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.btb.chalKak.common.exception.type.SuccessCode.*;

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
    public ResponseEntity<?> getComments(
        @PathVariable("postId") Long postId) {

        List<CommentLoadResponse> commentLoadResponses = commentService.getComments(postId);

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
}
