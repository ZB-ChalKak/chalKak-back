package com.btb.chalKak.domain.post.service;

import com.btb.chalKak.domain.post.dto.request.EditPostRequest;
import com.btb.chalKak.domain.post.dto.request.LoadPublicFeaturedPostsRequest;
import com.btb.chalKak.domain.post.dto.request.WritePostRequest;
import com.btb.chalKak.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    
    Post write(Authentication authentication, WritePostRequest request, MultipartFile[] multipartFileList);

    Post edit(Authentication authentication, Long postId, EditPostRequest request, MultipartFile[] multipartFileList);

    Page<Post> loadPublicPostsOrderByDesc(Pageable pageable);

    Post loadPublicPostDetails(Authentication authentication, Long postId);

    void delete(Authentication authentication, Long postId);

    Page<Post> loadPublicFeaturedPostsByKeywords(Pageable pageable, LoadPublicFeaturedPostsRequest request);

}
