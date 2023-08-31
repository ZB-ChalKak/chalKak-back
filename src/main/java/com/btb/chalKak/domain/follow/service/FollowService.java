package com.btb.chalKak.domain.follow.service;

import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_MEMBER_ID;
import static com.btb.chalKak.common.exception.type.ErrorCode.NOT_FOUND_FOLLOW_ID;

import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.domain.follow.entity.Follow;
import com.btb.chalKak.domain.follow.repository.FollowRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.member.service.Impl.MemberServiceImpl;
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
    public boolean followMember(Authentication authentication, Long followerId){

        Member member = memberService.getMemberByAuthentication(authentication);

        Long followingId = member.getId();

        // 이미 팔로우가 있는지 확인
        if(followRepository.existsByFollowingIdAndFollowerId(followingId, followerId)) {
            throw new RuntimeException("ALREADY_Followed");
        }

        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));


        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));

        followRepository.save(Follow.builder()
            .following(following)
            .follower(follower)
            .build());

        return true;
    }

    @Transactional
    public boolean unfollowMember(Authentication authentication, Long followerId){

        Member member = memberService.getMemberByAuthentication(authentication);

        Long followingId = member.getId();

        int deletedCount = followRepository.deleteByFollowingIdAndFollowerId(followingId, followerId);

        if(deletedCount == 0){
            throw new MemberException(NOT_FOUND_FOLLOW_ID);
        }

        return true;
    }
}
