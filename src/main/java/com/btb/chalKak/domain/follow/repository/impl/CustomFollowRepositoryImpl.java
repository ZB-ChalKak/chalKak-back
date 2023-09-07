package com.btb.chalKak.domain.follow.repository.impl;

import com.btb.chalKak.domain.follow.entity.QFollow;
import com.btb.chalKak.domain.follow.repository.CustomFollowRepository;
import com.btb.chalKak.domain.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomFollowRepositoryImpl implements CustomFollowRepository {

    private final JPAQueryFactory queryFactory;

    QMember qMember = QMember.member;
    QFollow qFollow = QFollow.follow;

    @Override
    public Page<Long> findFollowingIdsByFollowerId(Long followerId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Long> followingIds = queryFactory.select(qMember.id)
                .from(qFollow)
                .innerJoin(qFollow.following, qMember)
                .where(qFollow.follower.id.eq(followerId))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        long totalCount = followingIds.size();

        return new PageImpl<>(followingIds, pageRequest, totalCount);
    }

}
