package com.btb.chalKak.domain.comment.service;

import com.btb.chalKak.domain.comment.dto.request.CreateCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.DeleteCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.ModifyCommentRequest;
import com.btb.chalKak.domain.comment.dto.response.CommentLoadResponse;
import com.btb.chalKak.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface CommentService {

    Comment createComment(Authentication authentication,CreateCommentRequest request);

    List<CommentLoadResponse> loadComments(Long postId);

    Comment modifyComment(Authentication authentication, ModifyCommentRequest request);

    boolean deleteComment(Authentication authentication, DeleteCommentRequest request);

    Page<Comment> loadCommentsOrderByDesc(Long postId, Pageable pageable);
}
