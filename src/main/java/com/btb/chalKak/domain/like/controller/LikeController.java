package com.btb.chalKak.domain.like.controller;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.like.dto.LikeResponse;
import com.btb.chalKak.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LIKE;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_UNLIKE;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final ResponseService responseService;
    private final LikeService likeService;

    @GetMapping("/posts/{postId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> memberLikesPost(
//                            @RequestHeader("Authorization") String token,
        Authentication authentication, @PathVariable Long postId) {

        LikeResponse data = likeService.likePost(authentication, postId);

        CommonResponse<?> response = responseService.success(data, SUCCESS_LIKE);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> memberUnlikesPost(
            Authentication authentication, @PathVariable Long postId) {

        boolean isDeleted = likeService.unlikePost(
//                                token,
            authentication, postId);


        CommonResponse<?> response = responseService.success(isDeleted, SUCCESS_UNLIKE);
        return ResponseEntity.ok(response);
    }
}
