package com.btb.chalKak.domain.post.repository;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {

    void addViewCountFromRedis(Long postId, Long postViewCount);

    Page<Post> loadPublicPostsOrderByDesc(int page, int size);

    Optional<Post> loadPublicPostDetails(Long postId);

    Page<Post> loadPublicFeaturedPostsByMemberStyleTags(int page, int size, Member member);

    Page<Post> loadPublicFeaturedPostsByMember(int page, int size, Member member);

    Page<Post> loadPublicFeaturedPostsByBodyTypeAndStyleTags(int page, int size, double height,
            double weight, List<Long> styleTagIds, Member member);

}
