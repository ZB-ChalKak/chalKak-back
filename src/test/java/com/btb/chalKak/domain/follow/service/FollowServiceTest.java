package com.btb.chalKak.domain.follow.service;

import com.btb.chalKak.domain.follow.entity.Follow;
import com.btb.chalKak.domain.follow.repository.FollowRepository;
import com.btb.chalKak.domain.like.entity.Like;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FollowService followService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("팔로우 생성")
    void followMember() {

        // Given
        Long followingId = 1L;
        Long followerId = 2L;

        Member following = Member.builder()
                .id(followingId).build();
        Member follower = Member.builder()
                .id(followerId).build();

        Follow expectedFollow = Follow.builder().follower(follower).following(following).build();
        given(followRepository.save(any(Follow.class))).willReturn(expectedFollow);

        given(memberRepository.findById(followingId)).willReturn(Optional.of(following));
        given(memberRepository.findById(followerId)).willReturn(Optional.of(follower));
        given(followRepository.existsByFollowingIdAndFollowerId(followingId, followerId)).willReturn(false);

        // When
        Follow result = followService.followMember(followingId, followerId);

        // Then
        assertNotNull(result);
        assertEquals(expectedFollow, result);
    }

    @Test
    @DisplayName("팔로우 삭제")
    void unfollowMember() {
        // Given
        Long followingId = 20L;
        Long followerId = 15L;

        given(followRepository.deleteByFollowingIdAndFollowerId(followingId, followerId)).willReturn(1);

        // When
        boolean result = followService.unfollowMember(followingId, followerId);

        // Then
        assertTrue(result);
        verify(followRepository, times(1)).deleteByFollowingIdAndFollowerId(followingId, followerId);

    }
}