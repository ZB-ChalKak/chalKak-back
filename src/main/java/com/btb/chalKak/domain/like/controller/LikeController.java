package com.btb.chalKak.domain.like.controller;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.like.entity.Like;
import com.btb.chalKak.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_LIKE;
import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_UNLIKE;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final ResponseService responseService;
    private final LikeService likeService;

    @GetMapping("/{memberId}/posts/{postId}")
    public ResponseEntity<?> memberLikesPost(
//                            @RequestHeader("Authorization") String token,
            @PathVariable Long memberId, @PathVariable Long postId) {

        Like data = likeService.likePost(memberId,postId);

        CommonResponse<?> response = responseService.success(data, SUCCESS_LIKE);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}/posts/{postId}")
    public ResponseEntity<?> memberUnlikesPost(
//                            @RequestHeader("Authorization") String token,
            @PathVariable Long memberId, @PathVariable Long postId) {

        boolean isDeleted = likeService.unlikePost(
//                                token,
                                memberId,postId);


        CommonResponse<?> response = responseService.success(isDeleted, SUCCESS_UNLIKE);
        return ResponseEntity.ok(response);
    }
}
