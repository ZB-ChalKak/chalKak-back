package com.btb.chalKak.domain.post.repository.impl;

import static com.btb.chalKak.domain.post.type.PostStatus.PUBLIC;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.entity.QPost;
import com.btb.chalKak.domain.post.repository.CustomPostRepository;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    // 편차(Deviation), 키 1CM 당 0.8KG 증가를 가정
    private static final double HEIGHT_DEVIATION = 5.0;
    private static final double WEIGHT_DEVIATION = 4.0;

    // 가중치
    private static final double LIKE_WEIGHT = 0.6;
    private static final double VIEW_WEIGHT = 0.4;


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

        long totalCount = posts.size();

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

    @Override
    public Page<Post> loadPublicFeaturedPostsByMemberStyleTags(int page, int size, Member member) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Long> styleTagIds = getStyleTagIdsByMember(member);

        BooleanExpression publicPostsFilter = getPublicPostFilter();
        BooleanExpression similarStyleTags = QPost.post.styleTags.any().id.in(styleTagIds);

        List<Post> featuredPosts = queryFactory
                .select(QPost.post)
                .from(QPost.post)
                .where(similarStyleTags.and(publicPostsFilter))
                .orderBy(QPost.post.likeCount.multiply(LIKE_WEIGHT)
                        .add(QPost.post.viewCount.multiply(VIEW_WEIGHT))
                        .desc(),
                        QPost.post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        long totalCount = featuredPosts.size();

        // 추천 게시물이 존재하지 않을 경우 최신 게시물 조회
        if (totalCount < 1) {
            return loadPublicPostsOrderByDesc(page, size);
        }

        return new PageImpl<>(featuredPosts, pageRequest, totalCount);
    }

    @Override
    public Page<Post> loadPublicFeaturedPostsByMember(int page, int size, Member member) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Long> styleTagIds = getStyleTagIdsByMember(member);
        List<Post> featuredPosts = getFeaturedPostsByBodyTypeAndStyleTags(member.getHeight(), member.getWeight(), styleTagIds, pageRequest);

        long totalCount = featuredPosts.size();
        
        // 추천 게시물이 존재하지 않을 경우 회원 스타일 태그를 통한 추천 게시물 응답
        if (totalCount < 1) {
            return loadPublicFeaturedPostsByMemberStyleTags(page, size, member);
        }

        return new PageImpl<>(featuredPosts, pageRequest, totalCount);
    }

    @Override
    public Page<Post> loadPublicFeaturedPostsByBodyTypeAndStyleTags(int page, int size, double height,
            double weight, List<Long> styleTagIds, Member member) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Post> featuredPosts = getFeaturedPostsByBodyTypeAndStyleTags(height, weight, styleTagIds, pageRequest);

        long totalCount = featuredPosts.size();

        // 추천 게시물이 존재하지 않을 경우 회원 스타일 태그를 통한 추천 게시물 응답
        if (totalCount < 1) {
            return loadPublicFeaturedPostsByMember(page, size, member);
        }

        return new PageImpl<>(featuredPosts, pageRequest, totalCount);
    }

    @Override
    public Page<Post> loadLatestPublicPostsByMemberIds(List<Long> memberIds, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        BooleanExpression publicPostsFilter = getPublicPostFilter();

        List<Post> posts = queryFactory.selectFrom(qPost)
                .where(qPost.writer.id.in(memberIds)
                        .and(publicPostsFilter))
                .orderBy(qPost.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        long totalCount = posts.size();

        return new PageImpl<>(posts, pageRequest, totalCount);
    }

    private List<Post> getFeaturedPostsByBodyTypeAndStyleTags(double height, double weight, List<Long> styleTagIds,
            PageRequest pageRequest) {
        Double minHeight = height == 0 ? 0 : height - HEIGHT_DEVIATION;
        Double maxHeight = height == 0 ? Double.MAX_VALUE : height + HEIGHT_DEVIATION;
        Double minWeight = weight == 0 ? 0 : weight - WEIGHT_DEVIATION;
        Double maxWeight = weight == 0 ? Double.MAX_VALUE : weight + WEIGHT_DEVIATION;

        BooleanExpression publicPostsFilter = getPublicPostFilter();
        BooleanExpression styleTagsFilter = QPost.post.styleTags.any().id.in(styleTagIds);
        BooleanExpression similarHeight = QPost.post.writer.height.between(minHeight, maxHeight);
        BooleanExpression similarWeight = QPost.post.writer.weight.between(minWeight, maxWeight);

        return queryFactory
                .select(QPost.post)
                .from(QPost.post)
                .where(similarHeight
                        .and(similarWeight)
                        .and(styleTagsFilter)
                        .and(publicPostsFilter))
                .orderBy(QPost.post.likeCount.multiply(LIKE_WEIGHT)
                        .add(QPost.post.viewCount.multiply(VIEW_WEIGHT))
                        .desc(),
                        QPost.post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
    }

    private List<Post> getFeaturedPostsByStyleTags(List<Long> styleTagIds,
            PageRequest pageRequest) {
        BooleanExpression publicPostsFilter = getPublicPostFilter();
        BooleanExpression styleTagsFilter = QPost.post.styleTags.any().id.in(styleTagIds);

        return queryFactory
                .select(QPost.post)
                .from(QPost.post)
                .where(publicPostsFilter
                        .and(styleTagsFilter))
                .orderBy(QPost.post.likeCount.multiply(LIKE_WEIGHT)
                                .add(QPost.post.viewCount.multiply(VIEW_WEIGHT))
                                .desc(),
                        QPost.post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
    }

    private BooleanExpression getPublicPostFilter() {
        return qPost.status.eq(PUBLIC);
    }

    private List<Long> getStyleTagIdsByMember(Member member) {
        return member.getStyleTags().stream()
                .map(StyleTag::getId)
                .collect(Collectors.toList());
    }

}
