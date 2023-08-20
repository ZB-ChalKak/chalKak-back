package com.btb.chalKak.domain.post.controller;

import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS;
import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_SAVE_POST;

import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.post.dto.request.SavePostRequest;
import com.btb.chalKak.domain.post.dto.response.LoadPostDetailsResponse;
import com.btb.chalKak.domain.post.dto.response.SavePostResponse;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<?> savePost(@RequestBody SavePostRequest request) {
        Post post = postService.savePost(request);
        SavePostResponse data = SavePostResponse.builder()
                .postId(post.getId())
                .build();

        return ResponseEntity.ok(responseService.success(data, SUCCESS_SAVE_POST));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> loadPostDetails(@PathVariable Long postId) {
        Post post = postService.loadPostDetails(postId);
        LoadPostDetailsResponse data = LoadPostDetailsResponse.fromEntity(post);

        return ResponseEntity.ok(responseService.success(data, SUCCESS));
    }

}
