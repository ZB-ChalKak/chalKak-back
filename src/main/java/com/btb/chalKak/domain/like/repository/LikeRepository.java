package com.btb.chalKak.domain.like.repository;


import com.btb.chalKak.domain.like.entity.Like;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);

    int deleteByMemberIdAndPostId(Long memberId, Long PostId);
    boolean existsByMemberIdAndPostId(Long memberId, Long PostId);

    Page<Like> findAllByPostId(Long postId, Pageable pageable);

}
