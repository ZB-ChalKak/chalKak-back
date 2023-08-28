package com.btb.chalKak.domain.post.controller;

import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_DELETE_POST;
import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_LOAD_POST;
import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_WRITE_POST;

import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.post.dto.request.WritePostRequest;
import com.btb.chalKak.domain.post.dto.response.LoadPublicPostDetailsResponse;
import com.btb.chalKak.domain.post.dto.response.LoadPublicPostsResponse;
import com.btb.chalKak.domain.post.dto.response.WritePostResponse;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ResponseService responseService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> write(
            Authentication authentication,
            @RequestBody WritePostRequest request)
    {
        Post post = postService.write(authentication, request);
        WritePostResponse data = WritePostResponse.builder()
                .postId(post.getId())
                .build();

        return ResponseEntity.ok(responseService.success(data, SUCCESS_WRITE_POST));
    }

    @GetMapping
    public ResponseEntity<?> loadPublicPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postService.loadPublicPosts(pageRequest);
        LoadPublicPostsResponse data = LoadPublicPostsResponse.fromPage(posts);

        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_POST));
    }

    @GetMapping("/{postId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> loadPublicPostDetails(@PathVariable Long postId) {
        Post post = postService.loadPublicPostDetails(postId);
        LoadPublicPostDetailsResponse data = LoadPublicPostDetailsResponse.fromEntity(post);

        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_POST));
    }

    @PatchMapping("/{postId}/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deletePost(
            Authentication authentication, @PathVariable String postId) {
        postService.delete(authentication, Long.valueOf(postId));
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_DELETE_POST));
    }
}
