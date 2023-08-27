package com.btb.chalKak.domain.follow.controller;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.follow.entity.Follow;
import com.btb.chalKak.domain.follow.service.FollowService;
import com.btb.chalKak.domain.like.entity.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_FOLLOW;
import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_UNFOLLOW;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final ResponseService responseService;

    private final FollowService followservice;

    @GetMapping("/{followingId}/{followerId}")
    public ResponseEntity<?> followMember(
//                            @RequestHeader("Authorization") String token,
            @PathVariable Long followingId, @PathVariable Long followerId) {

        Follow data = followservice.followMember(followingId,followerId);

        CommonResponse<?> response = responseService.success(data, SUCCESS_FOLLOW);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{followingId}/{followerId}")
    public ResponseEntity<?> unfollowMember(
//                            @RequestHeader("Authorization") String token,
            @PathVariable Long followingId, @PathVariable Long followerId) {

        boolean isDeleted = followservice.unfollowMember(
//                                token,
                             followingId,followerId);


        CommonResponse<?> response = responseService.success(isDeleted, SUCCESS_UNFOLLOW);
        return ResponseEntity.ok(response);
    }
}
