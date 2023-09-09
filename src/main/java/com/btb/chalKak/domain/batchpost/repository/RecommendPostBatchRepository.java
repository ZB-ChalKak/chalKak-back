package com.btb.chalKak.domain.batchpost.repository;


import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendPostBatchRepository extends JpaRepository<RecommendPostBatch, Long> {
}
