package com.btb.chalKak.domain.user.repository;

import com.btb.chalKak.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
