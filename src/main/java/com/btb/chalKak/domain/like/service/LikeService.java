package com.btb.chalKak.domain.like.service;

import com.btb.chalKak.domain.like.entity.Like;
import com.btb.chalKak.domain.like.repository.LikeRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Like likePost(Long memberId, Long postId){
        // 이미 좋아요가 있는지 확인
        if(likeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new RuntimeException("ALREADY_LIKED");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member with id " + memberId + " not found"));

        Post post  = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post with id " + postId + " not found"));

        return likeRepository.save(Like.builder()
                .member(member)
                .post(post)
                .build());
    }

    @Transactional
    public boolean unlikePost(Long memberId, Long postId){
        try {
            int deletedCount = likeRepository.deleteByMemberIdAndPostId(memberId, postId);
            return deletedCount > 0;
        } catch (Exception e) {
            log.info("unlike 목록이 없습니다.");
            return false;
        }
    }

}
