package com.btb.chalKak.domain.comment.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.btb.chalKak.domain.comment.dto.request.CreateCommentRequest;
import com.btb.chalKak.domain.comment.dto.request.ModifyCommentRequest;
import com.btb.chalKak.domain.comment.entity.Comment;
import com.btb.chalKak.domain.comment.repository.CommentRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.post.service.impl.PostServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

  @InjectMocks
  private CommentServiceImpl commentService;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private PostRepository postRepository;

  @Mock
  private PostServiceImpl postService;


  @Test
  @DisplayName("댓글 생성")
  void createComment() {

    Long memberId = 20L;
    Member member = Member.builder()
        .id(memberId)
        .build();

    given(memberRepository.findById(memberId))
        .willReturn(Optional.of(member));

    Long postId = 20L;

    Post post = Post.builder()
        .id(postId)
        .build();

    given(postRepository.findById(postId))
        .willReturn(Optional.of(post));



    CreateCommentRequest request = CreateCommentRequest.builder()
        .content("댓글")
        .memberId(20L)
        .postId(20L)
        .build();

    Post post1 = postRepository.findById(request.getPostId())
        .orElseThrow(()-> new RuntimeException("not post"));

    Member member1 = memberRepository.findById(request.getMemberId())
        .orElseThrow(()->new RuntimeException("not member"));

    Comment comment = Comment.builder()
        .id(15L)
        .post(post1)
        .member(member1)
        .comment(request.getContent())
        .build();

    given(commentRepository.save(any(Comment.class)))
        .willReturn(comment);

    // when
    Comment saved = commentService.createComment(request);

    // then
    assertEquals(request.getContent(), saved.getComment());


  }

  @Test
  @DisplayName("댓글 호출")
  void getComments() {

    //given

    Long memberId = 20L;
    Member member = Member.builder()
        .id(memberId)
        .build();

    given(memberRepository.findById(memberId))
        .willReturn(Optional.of(member));

    Long postId = 20L;

    Post post = Post.builder()
        .id(postId)
        .build();

    given(postRepository.findById(postId))
        .willReturn(Optional.of(post));


    CreateCommentRequest request = CreateCommentRequest.builder()
        .content("댓글")
        .memberId(20L)
        .postId(20L)
        .build();

    Post post1 = postRepository.findById(request.getPostId())
        .orElseThrow(()-> new RuntimeException("not post"));

    Member member1 = memberRepository.findById(request.getMemberId())
        .orElseThrow(()->new RuntimeException("not member"));

    Comment comment = Comment.builder()
        .id(15L)
        .post(post1)
        .member(member1)
        .comment(request.getContent())
        .build();

    List<Comment> comments = new ArrayList<>();

    comments.add(comment);
    comments.add(comment);
    comments.add(comment);

    given(commentRepository.save(any(Comment.class)))
        .willReturn(comment);

    given(commentRepository.findCommentByPostId(postId))
        .willReturn(comments);

    // when
    commentService.createComment(request);
    commentService.createComment(request);
    commentService.createComment(request);

    List<Comment> result = commentService.getComments(postId);

    //then
    assertEquals(result, comments);


  }

  @Test
  @DisplayName("댓글 수정")
  void modifyComment() {
    //given

    Long memberId = 20L;
    Member member = Member.builder()
        .id(memberId)
        .build();

    given(memberRepository.findById(memberId))
        .willReturn(Optional.of(member));

    Long postId = 20L;

    Post post = Post.builder()
        .id(postId)
        .build();

    given(postRepository.findById(postId))
        .willReturn(Optional.of(post));


    CreateCommentRequest request = CreateCommentRequest.builder()
        .content("댓글")
        .memberId(20L)
        .postId(20L)
        .build();

    Post post1 = postRepository.findById(request.getPostId())
        .orElseThrow(()-> new RuntimeException("not post"));

    Member member1 = memberRepository.findById(request.getMemberId())
        .orElseThrow(()->new RuntimeException("not member"));

    Long commentId = 20L;
    Comment comment = Comment.builder()
        .id(commentId)
        .post(post1)
        .member(member1)
        .comment(request.getContent())
        .build();

    given(commentRepository.findById(commentId))
        .willReturn(Optional.of(comment));

    ModifyCommentRequest modifiedRequest = ModifyCommentRequest.builder()
        .content("댓글2313")
        .commentId(commentId)
        .memberId(20L)
        .build();

    Comment mdComment = Comment.builder()
        .id(commentId)
        .post(post1)
        .member(member1)
        .comment(modifiedRequest.getContent())
        .build();

    given(commentRepository.save(any(Comment.class)))
        .willReturn(mdComment);

    //when

    Comment result = commentService.modifyComment(modifiedRequest);

    //then

    assertEquals(result.getComment(),modifiedRequest.getContent());

  }
}