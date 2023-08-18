package com.btb.chalKak.domain.post.service;

import com.btb.chalKak.domain.post.dto.request.SavePostRequest;

public interface PostService {

    Long savePost(SavePostRequest request);

}
