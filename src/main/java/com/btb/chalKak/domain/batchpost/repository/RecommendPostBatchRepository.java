package com.btb.chalKak.domain.batchpost.repository;


import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.post.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendPostBatchRepository extends JpaRepository<RecommendPostBatch, Long> {

  @Query("SELECT p.id FROM RecommendPostBatch p " +
      "WHERE p.weatherId = :weatherId " +
      "ORDER BY (p.viewCount + p.likeCount) DESC")
  List<Long> findPostsByWeatherId(@Param("seasonId") Long weatherId);

  @Query("SELECT p FROM Post p WHERE p.id IN " +
          "(SELECT r.id FROM RecommendPostBatch r WHERE r.weatherId = :weatherId) " +
          "ORDER BY (p.viewCount * 0.4 + p.likeCount * 0.6) DESC")
  Page<Post> findPostsByWeatherId(Long weatherId, Pageable pageable);
}
