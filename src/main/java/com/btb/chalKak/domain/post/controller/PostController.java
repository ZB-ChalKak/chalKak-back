package com.btb.chalKak.domain.post.controller;

import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_SAVE_POST;

import com.btb.chalKak.domain.post.dto.PostDto;
import com.btb.chalKak.domain.post.dto.request.SavePostRequest;
import com.btb.chalKak.domain.post.dto.response.SavePostResponse;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.service.PostService;
import com.btb.chalKak.common.mapper.PostMapper;
import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
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
        Post post = postService.savePost(request);
        PostDto postDto = PostMapper.MAPPER.toDto(post);
        SavePostResponse data = SavePostResponse.builder()
                .postId(postDto.getId())
                .build();

        CommonResponse<?> response = responseService.success(data, SUCCESS_SAVE_POST);
        return ResponseEntity.ok(response);
    }

}
