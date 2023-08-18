package com.btb.chalKak.domain.post.controller;

import static com.btb.chalKak.global.response.type.SuccessCode.SUCCESS_SAVE_POST;

import com.btb.chalKak.domain.post.dto.request.SavePostRequest;
import com.btb.chalKak.domain.post.dto.response.SavePostResponse;
import com.btb.chalKak.domain.post.service.PostService;
import com.btb.chalKak.global.response.dto.CommonResponse;
import com.btb.chalKak.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        SavePostResponse data = SavePostResponse.builder()
                .postId(postService.savePost(request))
                .build();

        CommonResponse<?> response = responseService.success(data, SUCCESS_SAVE_POST);
        return ResponseEntity.ok(response);
    }

}
