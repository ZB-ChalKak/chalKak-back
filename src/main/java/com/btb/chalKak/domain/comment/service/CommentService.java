package com.btb.chalKak.domain.comment.service;

import com.btb.chalKak.domain.comment.dto.request.CreateCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.DeleteCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.ModifyCommentRequest;
import com.btb.chalKak.domain.comment.entity.Comment;
import java.util.List;

public interface CommentService {

    Comment createComment(CreateCommentRequest request);

    List<Comment> getComments(Long postId);

    Comment modifyComment(ModifyCommentRequest request);

    boolean deleteComment(DeleteCommentRequest request);
}
