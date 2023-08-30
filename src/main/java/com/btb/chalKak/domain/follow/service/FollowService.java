package com.btb.chalKak.domain.follow.service;

import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.domain.follow.entity.Follow;
import com.btb.chalKak.domain.follow.repository.FollowRepository;
import com.btb.chalKak.domain.like.entity.Like;
import com.btb.chalKak.domain.like.repository.LikeRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.member.service.Impl.MemberServiceImpl;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService {

    private final MemberRepository memberRepository;

    private final MemberServiceImpl memberService;

    private final FollowRepository followRepository;
    @Transactional
    public Follow followMember(Authentication authentication, Long followerId){

        Member member = memberService.getMemberByAuthentication(authentication);

        Long followingId = member.getId();

        // 이미 팔로우가 있는지 확인
        if(followRepository.existsByFollowingIdAndFollowerId(followingId, followerId)) {
            throw new RuntimeException("ALREADY_Followed");
        }

        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("following with id " + followingId + " not found"));


        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("follower with id " + followerId + " not found"));

        return followRepository.save(Follow.builder()
                        .following(following)
                        .follower(follower)
                        .build());
    }

    @Transactional
    public boolean unfollowMember(Authentication authentication, Long followerId){

        Member member = memberService.getMemberByAuthentication(authentication);

        Long followingId = member.getId();

        try {
            int deletedCount = followRepository.deleteByFollowingIdAndFollowerId(followingId, followerId);
            return deletedCount > 0;
        } catch (Exception e) {
            log.info("unFollow 목록이 없습니다.");
            return false;
        }
    }
}
