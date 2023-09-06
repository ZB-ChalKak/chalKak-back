package com.btb.chalKak.domain.follow.service;

import static com.btb.chalKak.common.exception.type.ErrorCode.ALREADY_FOLLOW;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_MEMBER_ID;
import static com.btb.chalKak.common.exception.type.ErrorCode.NOT_FOLLOW_SELF;
import static com.btb.chalKak.common.exception.type.ErrorCode.NOT_FOUND_FOLLOW_ID;

import java.util.Optional;

import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.domain.follow.dto.response.FollowerResponse;
import com.btb.chalKak.domain.follow.dto.response.LoadPageFollowResponse;
import com.btb.chalKak.domain.follow.entity.Follow;
import com.btb.chalKak.domain.follow.repository.FollowRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.member.service.Impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService {

    private final MemberRepository memberRepository;

    private final MemberServiceImpl memberService;

    private final FollowRepository followRepository;

    @Transactional
    public boolean followMember(Authentication authentication, Long followingId) {

        Member member = memberService.getMemberByAuthentication(authentication);

        Long followerId = member.getId();

        if(followingId == followerId){
            throw new MemberException(NOT_FOLLOW_SELF);
        }

        // 이미 팔로우가 있는지 확인
        if (followRepository.existsByFollowingIdAndFollowerId(followingId, followerId)) {
            throw new MemberException(ALREADY_FOLLOW);
        }

        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));

        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER_ID));

        followRepository.save(Follow.builder()
                .following(following)
                .follower(follower)
                .build());

        return true;
    }

    @Transactional
    public boolean unfollowMember(Authentication authentication, Long followingId) {

        Member member = memberService.getMemberByAuthentication(authentication);

        Long followerId = member.getId();

        int deletedCount = followRepository.deleteByFollowingIdAndFollowerId(followingId,
                followerId);

        if (deletedCount == 0) {
            throw new MemberException(NOT_FOUND_FOLLOW_ID);
        }

        return true;
    }

    @Transactional(readOnly = true)
    public LoadPageFollowResponse loadFollowers(Long memberId, Pageable pageable) {

        Page<Long> membersId = followRepository.findFollowerIdsByFollowingId(memberId, pageable);

        List<Member> members = membersId.stream()
                .map(memberRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<FollowerResponse> followerResponses =
                members.stream()
                        .map(FollowerResponse::fromEntity)
                        .collect(Collectors.toList());

        return LoadPageFollowResponse.builder()
                .totalPages(membersId.getTotalPages())
                .currentPage(membersId.getNumber())
                .totalElements(membersId.getTotalElements())
                .followerResponses(followerResponses)
                .build();
    }

    @Transactional(readOnly = true)
    public LoadPageFollowResponse loadFollowings(Long memberId, Pageable pageable) {

        Page<Long> membersId = followRepository.findFollowingIdsByFollowerId(memberId, pageable);

        List<Member> members = membersId.stream()
                .map(memberRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<FollowerResponse> followerResponses =
                members.stream()
                        .map(FollowerResponse::fromEntity)
                        .collect(Collectors.toList());

        return LoadPageFollowResponse.builder()
                .totalPages(membersId.getTotalPages())
                .currentPage(membersId.getNumber())
                .totalElements(membersId.getTotalElements())
                .followerResponses(followerResponses)
                .build();
    }
}
