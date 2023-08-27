package com.btb.chalKak.domain.comment.service.impl;

import com.btb.chalKak.domain.comment.dto.request.CreateCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.DeleteCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.ModifyCommentRequest;
import com.btb.chalKak.domain.comment.entity.Comment;
import com.btb.chalKak.domain.comment.repository.CommentRepository;
import com.btb.chalKak.domain.comment.service.CommentService;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    private final PostRepository postRepository;

    @Override
    public Comment createComment(CreateCommentRequest request) {

        Member member = memberRepository.findById(request.getMemberId())
            .orElseThrow(()-> new RuntimeException("NOT_FOUND_MEMBER")); // TODO: 2023-08-19 Exception 제어 필요

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
    public List<Comment> getComments(Long postId) {

        List<Comment> comments = commentRepository.findCommentByPostId(postId);

        if (comments == null) {
            throw new RuntimeException("NOT_EXIST_COMMENT");  // TODO: 2023-08-19 Exception 제어 필요
        }

        return comments;
    }

    @Override
    @Transactional
    public Comment modifyComment(ModifyCommentRequest request) {

        // TODO: 2023-08-19 token으로 memberId 검증 필요

        Comment comment = commentRepository.findById(request.getCommentId())
            .orElseThrow(()-> new RuntimeException("NOT_EXIST_COMMENT"));

        comment.updateComment(request.getContent());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public boolean deleteComment(DeleteCommentRequest request) {

        // MemberId verification
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if(!comment.getMember().getId().equals(request.getMemberId())) {
            throw new RuntimeException("Unauthorized");
        }

        int deletedCount = commentRepository.deleteCommentById(request.getCommentId());

        return deletedCount > 0;
    }
//        @Override
//    @Transactional
//    public Long saveComment(SaveCommentRequest request) {
//        // 1. 회원 조회
//        Member member = getMemberById(request.getMemberId());
//
//
//
//        // 2. 스타일 태그 조회
//        List<StyleTag> styleTags = styleTagRepository.findAllById(request.getStyleTags());
//
//        // 3. 해시 태그 조회 및 업로드
//        List<HashTag> hashTags = new ArrayList<>();
//        for (String keyword : request.getHashTags()) {
//            HashTag hashTag = hashTagRepository.findByKeyword(keyword)
//                    .orElse(HashTag.builder()
//                            .keyword(keyword)
//                            .build());
//
//            hashTags.add(hashTag);
//        }
//        hashTagRepository.saveAll(hashTags);
//
//        // 4. 게시글 저장
//        Post post = postRepository.save(Post.builder()
//                .content(request.getContent())
//                .status(PUBLIC)
//                .writer(member)
//                .styleTags(styleTags)
//                .hashTags(hashTags)
//                .build());
//
//        return post.getId();
//    }
//
//    private Member getMemberById(Long memberId) {
//        return memberRepository.findById(memberId)
//                .orElseThrow(() -> new RuntimeException("CustomMemberException"));
//    }
}
