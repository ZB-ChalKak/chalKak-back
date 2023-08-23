package com.btb.chalKak.domain.post.service;

import com.btb.chalKak.domain.post.dto.request.SavePostRequest;
import com.btb.chalKak.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface PostService {
    
    // TODO 로그인 기능 개발 -> Authentication authentication 검증 추가

    Post savePost(SavePostRequest request);

    Page<Post> loadPublicPosts(Pageable pageable);

    Post loadPublicPostDetails(Long postId);

    void deletePost(Authentication authentication, Long postId);

}
