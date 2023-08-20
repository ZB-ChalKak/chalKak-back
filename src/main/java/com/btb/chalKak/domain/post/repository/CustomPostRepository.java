package com.btb.chalKak.domain.post.repository;

public interface CustomPostRepository {

    void addViewCntFromRedis(Long postId, Long postViewCnt);

}
