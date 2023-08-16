package com.btb.chalKak.domain.member.repository;

import com.btb.chalKak.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
