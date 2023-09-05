package com.btb.chalKak.domain.styleTag.repository;

import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StyleTagRepository extends JpaRepository<StyleTag, Long> {
    Optional<StyleTag> findByKeyword(String keyword);

    Page<StyleTag> findAllByKeywordContaining(String keyword, Pageable pageable);
}
