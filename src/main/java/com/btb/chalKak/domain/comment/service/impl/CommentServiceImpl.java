package com.btb.chalKak.domain.comment.service.impl;

import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_COMMENT_ID;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_MEMBER_ID;

import com.btb.chalKak.common.exception.CommentException;
import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.domain.comment.dto.request.CreateCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.DeleteCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.ModifyCommentRequest;
import com.btb.chalKak.domain.comment.dto.response.CommentLoadResponse;
import com.btb.chalKak.domain.comment.entity.Comment;
import com.btb.chalKak.domain.comment.repository.CommentRepository;
import com.btb.chalKak.domain.comment.service.CommentService;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.service.Impl.MemberServiceImpl;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final MemberServiceImpl memberService;
    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    @Override
    public Comment createComment(Authentication authentication, CreateCommentRequest request) {


        Member member = memberService.getMemberByAuthentication(authentication);
//            .orElseThrow(()-> new RuntimeException("NOT_FOUND_MEMBER")); // TODO: 2023-08-19 Exception 제어 필요

        Post post = postRepository.findById(request.getPostId())
            .orElseThrow(()-> new RuntimeException("NOT_FOUND_POST"));  // TODO: 2023-08-19 Exception 제어 필요

        Comment comment = Comment.builder()
            .comment(request.getContent())
            .member(member)
            .post(post)
            .build();

        return commentRepository.save(comment);
    }
    @Override
    @Transactional(readOnly = true)
    public List<CommentLoadResponse> loadComments(Long postId) {

        List<Comment> comments = commentRepository.findCommentByPostId(postId);

        List<CommentLoadResponse> commentLoadResponses = new ArrayList<>();

        for(Comment comment : comments){

            commentLoadResponses.add(CommentLoadResponse.builder()
                    .nickname(comment.getMember().getNickname())
                    .profileUrl(comment.getMember().getProfileImg())
                    .commentId(comment.getId())
                    .comment(comment.getComment())
                    .updatedAt(comment.getUpdatedAt())
                    .createAt(comment.getCreatedAt())
                    .build());
        }

        if (comments == null) {
            throw new CommentException(INVALID_COMMENT_ID);  // TODO: 2023-08-19 Exception 제어 필요
        }

        return commentLoadResponses;
    }

    @Override
    @Transactional
    public Comment modifyComment(Authentication authentication, ModifyCommentRequest request) {

        Member member = memberService.getMemberByAuthentication(authentication);

        Comment comment = commentRepository.findById(request.getCommentId())
            .orElseThrow(()-> new CommentException(INVALID_COMMENT_ID));

        if(!comment.getMember().getId().equals(member.getId())) {
            throw new MemberException(INVALID_MEMBER_ID);
        }

        comment.updateComment(request.getContent());

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public boolean deleteComment(Authentication authentication, Long commentId) {

        // MemberId verification
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(()-> new CommentException(INVALID_COMMENT_ID));

        Member member = memberService.getMemberByAuthentication(authentication);

        if(!comment.getMember().getId().equals(member.getId())) {
            throw new MemberException(INVALID_MEMBER_ID);
        }

        commentRepository.deleteById(commentId);

        return true;
    }
    @Override
    @Transactional(readOnly = true)
    public Page<Comment> loadCommentsOrderByDesc(Long postId, Pageable pageable) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
    }
}
