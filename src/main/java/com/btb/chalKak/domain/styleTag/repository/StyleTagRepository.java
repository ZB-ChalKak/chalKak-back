package com.btb.chalKak.domain.styleTag.repository;

import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StyleTagRepository extends JpaRepository<StyleTag, Long> {
    Optional<StyleTag> findByKeyword(String keyword);
}
