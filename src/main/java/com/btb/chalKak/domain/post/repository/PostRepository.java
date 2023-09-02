package com.btb.chalKak.domain.post.repository;

import com.btb.chalKak.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    List<Post> findAllByContentContaining(String keyword);
}
