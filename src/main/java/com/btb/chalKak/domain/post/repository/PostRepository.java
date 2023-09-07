package com.btb.chalKak.domain.post.repository;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    Page<Post> findAllByContentContaining(String keyword, Pageable pageable);

    @Query("SELECT COUNT(p.id) FROM Post p WHERE p.writer.id = :memberId")
    Long countPostIdsByMemberId(Long memberId);

    List<Post> findAllByWriter(Member writer);

    @Query("SELECT p FROM Post p " +
        "JOIN p.styleTags s " +
        "WHERE s.id = :weatherId AND s.id = :seasonId And s.id IN :styleTags " +
        "GROUP BY p " +
        "HAVING COUNT(s.id) = 2 OR COUNT(s.id) >= 1 " +
        "ORDER BY (p.viewCount + p.likeCount) DESC")
    Page<Post> findPostsByStyleTagsAndWeatherIdAndSeasonId(@Param("styleTags") List<Long> styleTags,
                                                           @Param("weatherId") Long weatherId,
                                                           @Param("seasonId") Long seasonId,
                                                           Pageable pageable);
}
