package com.btb.chalKak.domain.post.repository.impl;

import com.btb.chalKak.domain.post.entity.QPost;
import com.btb.chalKak.domain.post.repository.CustomPostRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    QPost post = QPost.post;

    @Override
    public void addViewCntFromRedis(Long postId, Long postViewCnt) {
        queryFactory
                .update(post)
                .set(post.viewCount, postViewCnt)
                .where(post.id.eq(postId))
                .execute();
    }
}
