package com.btb.chalKak.domain.member.repository;

import com.btb.chalKak.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일 중복 여부 확인
    boolean existsByEmail(String email);

    // 닉네임 중복 여부 확인
    boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Page<Member> findAllByNicknameContaining(String keyword, Pageable pageable);

    Optional<Member> findByNickname(String nickname);
}
