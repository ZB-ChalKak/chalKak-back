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
      "WHERE (s.id = :weatherId OR s.id = :seasonId OR s.id IN :styleTags) and p.status = 'PUBLIC'" +
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
      "WHERE (s.id = :weatherId OR s.id = :seasonId) and p.status = 'PUBLIC' " +
      "GROUP BY p " +
      "HAVING SUM(CASE WHEN s.id = :weatherId THEN 1 ELSE 0 END) >= 1 " +
      "AND SUM(CASE WHEN s.id = :seasonId THEN 1 ELSE 0 END) >= 1 " +
      "ORDER BY (p.viewCount + p.likeCount) DESC")
  Page<Post> findPostsAndWeatherIdAndSeasonId(
      @Param("weatherId") Long weatherId,
      @Param("seasonId") Long seasonId,
      Pageable pageable);


  @Query("SELECT p FROM Post p " +
//      "JOIN FETCH p.writer w " +  // writer 필드를 Eager 로딩
      "JOIN p.styleTags s " +
      "WHERE ( s.id = :seasonId ) and p.status = 'PUBLIC' " +
      "GROUP BY p " +
      "HAVING SUM(CASE WHEN s.id = :seasonId THEN 1 ELSE 0 END) >= 1 " +
      "AND (p.viewCount + p.likeCount) > :count ")
  List<Post> findPostsAndSeasonIdAndFetch(@Param("seasonId") Long seasonId, @Param("count") Long count);

  @Query("SELECT p FROM Post p " +
      "JOIN FETCH p.writer w " +
      "WHERE p.id IN :postIds " +
      "ORDER BY (p.viewCount + p.likeCount) DESC")
  List<Post> findPostsWithWritersByIds(@Param("postIds") List<Long> postIds);

  @Query("SELECT p FROM Post p " +
      "WHERE p.id IN :postIds AND p.status = 'PUBLIC' " +
      "ORDER BY (p.viewCount + p.likeCount) DESC")
  Page<Post> findPostsByIdsAndStatus(@Param("postIds") List<Long> postIds,Pageable pageable);

}
