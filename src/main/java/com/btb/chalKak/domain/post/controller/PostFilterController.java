package com.btb.chalKak.domain.post.controller;

import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_POST;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.post.dto.request.LoadPublicFeaturedPostsRequest;
import com.btb.chalKak.domain.post.dto.response.LoadPublicPostsResponse;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class PostFilterController {

    private final PostService postService;
    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<CommonResponse<?>> loadPublicFeaturedPostsByKeywords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
            , @RequestBody LoadPublicFeaturedPostsRequest request)
    {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postService.loadPublicFeaturedPostsByKeywords(authentication, pageRequest, request);
        LoadPublicPostsResponse data = LoadPublicPostsResponse.fromPage(posts);

        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_POST));
    }

}
