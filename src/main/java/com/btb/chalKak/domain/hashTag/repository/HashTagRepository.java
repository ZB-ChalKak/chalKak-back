package com.btb.chalKak.domain.hashTag.repository;

import com.btb.chalKak.domain.hashTag.entity.HashTag;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    Optional<HashTag> findByKeyword(String keyword);

    List<HashTag> findAllByKeywordContaining(String keyword);
}
