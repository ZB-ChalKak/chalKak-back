package com.btb.chalKak.domain.post.repository.impl;

import static com.btb.chalKak.domain.post.type.PostStatus.PUBLIC;

import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.entity.QPost;
import com.btb.chalKak.domain.post.repository.CustomPostRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    QPost qPost = QPost.post;

    @Override
    public void addViewCountFromRedis(Long postId, Long postViewCount) {
        queryFactory
                .update(qPost)
                .set(qPost.viewCount, postViewCount)
                .where(qPost.id.eq(postId))
                .execute();
    }

    @Override
    public Page<Post> loadPublicPostsOrderByDesc(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        BooleanExpression publicPostsFilter = getPublicPostFilter();

        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(publicPostsFilter)
                .orderBy(qPost.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .selectFrom(qPost)
                .where(publicPostsFilter)
                .fetch()
                .size();

        return new PageImpl<>(posts, pageRequest, totalCount);
    }

    @Override
    public Optional<Post> loadPublicPostDetails(Long postId) {
        BooleanExpression publicPostFilter = getPublicPostFilter();

        return Optional.ofNullable(queryFactory
                .selectFrom(qPost)
                .where(publicPostFilter.and(qPost.id.eq(postId)))
                .fetchOne());
    }

    private BooleanExpression getPublicPostFilter() {
        return qPost.status.eq(PUBLIC);
    }

}
