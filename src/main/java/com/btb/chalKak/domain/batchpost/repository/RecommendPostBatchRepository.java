package com.btb.chalKak.domain.batchpost.repository;


import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
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
}
