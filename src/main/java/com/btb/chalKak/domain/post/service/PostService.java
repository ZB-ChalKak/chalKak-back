package com.btb.chalKak.domain.post.service;

import com.btb.chalKak.domain.post.dto.request.SavePostRequest;
import com.btb.chalKak.domain.post.entity.Post;

public interface PostService {

    Post savePost(SavePostRequest request);

    Post loadPostDetails(Long postId);

}
