package com.btb.chalKak.domain.like.repository;


import com.btb.chalKak.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);

    int deleteByMemberIdAndPostId(Long memberId, Long PostId);
    boolean existsByMemberIdAndPostId(Long memberId, Long PostId);
}
