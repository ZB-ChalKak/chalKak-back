package com.btb.chalKak.domain.post.repository;

import com.btb.chalKak.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

}
