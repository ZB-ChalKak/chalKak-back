package com.btb.chalKak.domain.like.service;

import com.btb.chalKak.domain.like.entity.Like;
import com.btb.chalKak.domain.like.repository.LikeRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;


    @Test
    @DisplayName("좋아요 생성")
    void likePost() {

        //given
        Long memberId = 20L;
        Member member = Member.builder().id(memberId).build();

        Long postId = 20L;
        Post post = Post.builder().id(postId).build();

        Like expectedLike = Like.builder().member(member).post(post).build();

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(likeRepository.existsByMemberIdAndPostId(memberId, postId)).willReturn(false);
        given(likeRepository.save(any(Like.class))).willReturn(expectedLike);

        // when
        Like actualLike = likeService.likePost(memberId, postId);

        // then
        assertEquals(expectedLike, actualLike);
    }

    @Test
    @DisplayName("좋아요 삭제")
    void unlikePost() {

        // Given
        Long memberId = 20L;
        Long postId = 20L;

        given(likeRepository.deleteByMemberIdAndPostId(memberId, postId)).willReturn(1);

        // When
        boolean result = likeService.unlikePost(memberId, postId);

        // Then
        assertTrue(result);
        verify(likeRepository, times(1)).deleteByMemberIdAndPostId(memberId, postId);
    }
}