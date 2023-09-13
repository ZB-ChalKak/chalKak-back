package com.btb.chalKak.domain.post.controller;

import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_DELETE_POST;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_EDIT_POST;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_POST;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_WRITE_POST;

import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.post.dto.request.EditPostRequest;
import com.btb.chalKak.domain.post.dto.request.WritePostRequest;
import com.btb.chalKak.domain.post.dto.response.EditPostResponse;
import com.btb.chalKak.domain.post.dto.response.LoadPublicPostDetailsResponse;
import com.btb.chalKak.domain.post.dto.response.LoadPublicPostsResponse;
import com.btb.chalKak.domain.post.dto.response.WritePostResponse;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ResponseService responseService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> write(
            Authentication authentication,
            @RequestPart MultipartFile[] multipartFileList,
            @RequestPart WritePostRequest request)
    {
        Post post = postService.write(authentication, request, multipartFileList);
        WritePostResponse data = WritePostResponse.builder()
                .postId(post.getId())
                .build();

        return ResponseEntity.ok(responseService.success(data, SUCCESS_WRITE_POST));
    }

    @PatchMapping(value = "/{postId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> edit(
            Authentication authentication,
            @PathVariable Long postId,
            @RequestPart(value = "multipartFileList", required = false) MultipartFile[] multipartFileList,
            @RequestPart EditPostRequest request)
    {
        Post post = postService.edit(authentication, postId, request , multipartFileList);
        EditPostResponse data = EditPostResponse.builder()
                .postId(post.getId())
                .build();

        return ResponseEntity.ok(responseService.success(data, SUCCESS_EDIT_POST));
    }

    @GetMapping
    public ResponseEntity<?> loadPublicPostsOrderByDesc(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postService.loadPublicPostsOrderByDesc(authentication, pageRequest);
        LoadPublicPostsResponse data = LoadPublicPostsResponse.fromPage(posts);

        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_POST));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> loadPublicPostDetails(
            Authentication authentication,
            @PathVariable Long postId)
    {
        Post post = postService.loadPublicPostDetails(authentication, postId);
        LoadPublicPostDetailsResponse data = LoadPublicPostDetailsResponse.fromEntity(post);

        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_POST));
    }

    @PatchMapping("/{postId}/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deletePost(
            Authentication authentication,
            @PathVariable String postId)
    {
        postService.delete(authentication, Long.valueOf(postId));

        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_DELETE_POST));
    }

}
