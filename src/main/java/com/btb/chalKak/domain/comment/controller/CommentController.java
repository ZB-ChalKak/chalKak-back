package com.btb.chalKak.domain.comment.controller;

import static com.btb.chalKak.global.response.type.SuccessCode.SUCCESS_SAVE_POST;

import com.btb.chalKak.domain.comment.dto.request.CreateCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.DeleteCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.ModifyCommentRequest;
import com.btb.chalKak.domain.comment.dto.response.CommentResponse;
import com.btb.chalKak.domain.comment.entity.Comment;
import com.btb.chalKak.domain.comment.service.CommentService;
import com.btb.chalKak.global.response.dto.CommonResponse;
import com.btb.chalKak.global.response.service.ResponseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createComment(
//                            @RequestHeader("Authorization") String token,
                            @RequestBody CreateCommentRequest request) {
        CommentResponse data = CommentResponse.builder()
                .commentId(commentService.createComment(request).getId())
                .build();

        CommonResponse<?> response = responseService.success(data, SUCCESS_SAVE_POST);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getComments(
        @PathVariable("postId") Long postId) {

        List<Comment> comments = commentService.getComments(postId);

        CommonResponse<?> response = responseService.success(comments, SUCCESS_SAVE_POST);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comments")
    public ResponseEntity<?> modifyComment(
//                            @RequestHeader("Authorization") String token,
        @RequestBody ModifyCommentRequest request) {
        CommentResponse data = CommentResponse.builder()
            .commentId(commentService.modifyComment(request).getId())
            .build();

        CommonResponse<?> response = responseService.success(data, SUCCESS_SAVE_POST);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments")
    public ResponseEntity<?> deleteComments(
//                            @RequestHeader("Authorization") String token,
        @RequestBody DeleteCommentRequest request) {

        boolean isDeleted = commentService.deleteComment(
//                                token,
                                request);


        CommonResponse<?> response = responseService.success(isDeleted, SUCCESS_SAVE_POST);
        return ResponseEntity.ok(response);
    }
}
