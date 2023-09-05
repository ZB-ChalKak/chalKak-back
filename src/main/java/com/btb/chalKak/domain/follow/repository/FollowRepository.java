package com.btb.chalKak.domain.follow.repository;


import com.btb.chalKak.domain.follow.entity.Follow;
import com.btb.chalKak.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {


    boolean existsByFollowingIdAndFollowerId(Long followingId, Long followerId);

    int deleteByFollowingIdAndFollowerId(Long followingId, Long followerId);

    @Query("SELECT f FROM Follow f WHERE f.following.id = :followingId")
    List<Follow> findByFollowingId(@Param("followingId") Long followingId);

    Optional<Long> countByFollowingId(Long follow);

    Optional<Long> countByFollowerId(Long targetMemberId);

    @Query("SELECT f.follower.id FROM Follow f WHERE f.following.id = :followingId")
    Page<Long> findFollowerIdsByFollowingId(Long followingId, Pageable pageable);

    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId")
    Page<Long> findFollowingIdsByFollowerId(Long followerId, Pageable pageable);

}
