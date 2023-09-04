package com.btb.chalKak.domain.post.repository;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    List<Post> findAllByContentContaining(String keyword);

    List<Post> findAllByWriter(Member writer);
}
