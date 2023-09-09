package com.btb.chalKak.domain.post.repository;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import java.util.List;
import java.util.Optional;

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
        "WHERE (s.id = :weatherId OR s.id = :seasonId OR s.id IN :styleTags) " +
        "GROUP BY p " +
        "HAVING SUM(CASE WHEN s.id = :weatherId THEN 1 ELSE 0 END) >= 1 " +
        "AND SUM(CASE WHEN s.id = :seasonId THEN 1 ELSE 0 END) >= 1 " +
        "AND SUM(CASE WHEN s.id IN :styleTags THEN 1 ELSE 0 END) >= 1 " +
        "ORDER BY (p.viewCount + p.likeCount) DESC")
    Page<Post> findPostsByStyleTagsAndWeatherIdAndSeasonId(@Param("styleTags") List<Long> styleTags,
                                                           @Param("weatherId") Long weatherId,
                                                           @Param("seasonId") Long seasonId,
                                                           Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "JOIN p.styleTags s " +
            "WHERE ( s.id = :seasonId ) " +
            "GROUP BY p " +
            "HAVING SUM(CASE WHEN s.id = :seasonId THEN 1 ELSE 0 END) >= 1 " +
            "AND (p.viewCount + p.likeCount) > :count " +
            "ORDER BY (p.viewCount + p.likeCount) DESC")
    List<Post> findPostsAndSeasonId(@Param("seasonId") Long seasonId, @Param("count") Long count);

}
