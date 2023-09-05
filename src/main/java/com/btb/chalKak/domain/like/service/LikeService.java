package com.btb.chalKak.domain.like.service;

import java.util.*;

import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.common.exception.PostException;
import com.btb.chalKak.domain.follow.entity.Follow;
import com.btb.chalKak.domain.follow.repository.FollowRepository;
import com.btb.chalKak.domain.like.dto.LikeResponse;
import com.btb.chalKak.domain.like.dto.LikerResponse;
import com.btb.chalKak.domain.like.dto.LoadPageLikeResponse;
import com.btb.chalKak.domain.like.entity.Like;
import com.btb.chalKak.domain.like.repository.LikeRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.member.service.Impl.MemberServiceImpl;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.btb.chalKak.common.exception.type.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    private final FollowRepository followRepository;

    private final MemberServiceImpl memberService;

    @Transactional
    public LikeResponse likePost( Authentication authentication, Long postId){


        Member member = memberService.getMemberByAuthentication(authentication);

        Long memberId = member.getId();

        memberService.validateMemberId(authentication, memberId);

        // 이미 좋아요가 있는지 확인
        if(likeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new PostException(ALREADY_LIKE);
        }

        Post post  = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(INVALID_POST_ID));

        Like like = likeRepository.save(Like.builder()
            .member(member)
            .post(post)
            .build());

        return LikeResponse.builder()
            .likeId(like.getId())
            .memberId(member.getId())
            .postId(post.getId())
            .build();
    }

    @Transactional
    public boolean unlikePost( Authentication authentication, Long postId){

        Member member = memberService.getMemberByAuthentication(authentication);

        Long memberId = member.getId();

        int deletedCount = likeRepository.deleteByMemberIdAndPostId(memberId, postId);

        if(deletedCount == 0){
            throw new MemberException(NOT_FOUND_LIKE_ID);
        }

        return true;
    }

    @Transactional(readOnly = true)
    public LoadPageLikeResponse loadLikers(Authentication authentication, Long postId, Pageable pageable) {

        Member member = memberService.getMemberByAuthentication(authentication);

        Long memberId = member.getId();

        Map<Long, Long> followed = new HashMap<>();

        List<Follow> follows = followRepository.findByFollowingId(memberId);

        for(Follow follow : follows){
            followed.put(memberId,follow.getFollower().getId());
        }

// 1. Fetch Likes by postId
        Page<Like> likes = likeRepository.findAllByPostId(postId, pageable);

// 2. Convert Like entities to LikeDto
        List<LikeResponse> likeDtos = likes.getContent().stream()
            .map(LikeResponse::fromEntity)
            .collect(Collectors.toList());

// 3. Extract memberIds from LikeDto
        List<Long> memberIds = likeDtos.stream()
            .map(LikeResponse::getMemberId)
            .collect(Collectors.toList());

// 4. Fetch Members using memberIds
        List<Member> members = memberIds.stream()
            .map(id -> memberRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

// 5. Convert Members to LikerResponse
        List<LikerResponse> likerResponses = new LinkedList<>();

        for(Member curMember : members){
            LikerResponse likerResponse = LikerResponse.fromEntity(curMember);
            likerResponse.updateFollowed(followed.containsValue(curMember.getId()));
            likerResponses.add(likerResponse);
        }


// 6. Build the response
        return LoadPageLikeResponse.builder()
            .totalPages(likes.getTotalPages())
            .currentPage(likes.getNumber())
            .totalElements(likes.getTotalElements())
            .likerResponses(likerResponses)
            .build();
    }

}
