package com.btb.chalKak.domain.follow.repository;


import com.btb.chalKak.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {


    boolean existsByFollowingIdAndFollowerId(Long followingId, Long followerId);

    int deleteByFollowingIdAndFollowerId(Long followingId, Long followerId);

    Optional<Long> countByFollowingId(Long follow);

    Optional<Long> countByFollowerId(Long targetMemberId);
}
