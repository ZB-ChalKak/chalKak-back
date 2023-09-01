package com.btb.chalKak.domain.follow.controller;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.domain.comment.dto.response.LoadPageCommentsResponse;
import com.btb.chalKak.domain.follow.dto.response.LoadPageFollowResponse;
import com.btb.chalKak.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.btb.chalKak.common.exception.type.SuccessCode.*;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final ResponseService responseService;

    private final FollowService followService;

    @GetMapping("/{followerId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> followMember(
                            Authentication authentication,
                            @PathVariable Long followerId) {

        boolean data = followService.followMember(
                            authentication,
                            followerId);

        CommonResponse<?> response = responseService.success(data, SUCCESS_FOLLOW);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{followerId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> unfollowMember(
                    Authentication authentication,
                    @PathVariable Long followerId) {

        boolean isDeleted = followService.unfollowMember(
                                authentication,
                                followerId);


        CommonResponse<?> response = responseService.success(isDeleted, SUCCESS_UNFOLLOW);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/pageFollower")
    public ResponseEntity<?> loadFollowers(
            @PathVariable Long memberId, Pageable pageable)
    {
        LoadPageFollowResponse data = followService.loadFollowers(memberId, pageable);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_FOLLOWERS));
    }

    @GetMapping("/{memberId}/pageFollowing")
    public ResponseEntity<?> loadFollowings(
            @PathVariable Long memberId, Pageable pageable)
    {
        LoadPageFollowResponse data = followService.loadFollowings(memberId, pageable);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_COMMENT));
    }
}
