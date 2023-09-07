package com.btb.chalKak.domain.follow.repository;

import org.springframework.data.domain.Page;

public interface CustomFollowRepository {

    Page<Long> findFollowingIdsByFollowerId(Long followerId, int page, int size);
}
