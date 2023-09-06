package com.btb.chalKak.domain.member.controller;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.request.TokenRequestDto;
import com.btb.chalKak.common.util.ValidationUtils;
import com.btb.chalKak.domain.member.dto.request.*;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.dto.response.UserDetailsInfoResponse;
import com.btb.chalKak.domain.member.dto.response.UserInfoResponse;
import com.btb.chalKak.domain.member.dto.response.ValidateInfoResponse;
import com.btb.chalKak.domain.member.service.MemberService;
import com.btb.chalKak.domain.post.dto.response.LoadPublicPostsResponse;
import com.btb.chalKak.domain.post.entity.Post;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.btb.chalKak.common.exception.type.SuccessCode.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class SignController {

    private final MemberService memberService;
    private final ResponseService responseService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody SignUpMemberRequest request
            , BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationUtils.getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.failure(errorMessage));
        }

        memberService.signUp(request);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_SAVE_MEMBER));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInMemberRequest request){
        SignInMemberResponse data = memberService.SignIn(request);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_SIGN_IN));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        TokenDto data = memberService.reissue(tokenRequestDto);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_REISSUE));
    }

    @PutMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        memberService.signOut(request);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_SIGN_OUT));
    }

    @GetMapping("/validate/email/{email}")
    public ResponseEntity<?> validateEmail(@PathVariable String email){
        ValidateInfoResponse data = memberService.validateEmail(email);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_VALIDATE_EMAIL));
    }

    @GetMapping("/validate/nickname/{nickname}")
    public ResponseEntity<?> validateNickname(@PathVariable String nickname){
        ValidateInfoResponse data = memberService.validateNickname(nickname);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_VALIDATE_NICKNAME));
    }

    @GetMapping("/details/{userId}")
    public ResponseEntity<?> userDetailsInfo(@PathVariable Long userId){
        UserDetailsInfoResponse data = memberService.userDetailsInfo(userId);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_USER_DETAILS_INFO));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> userInfo(HttpServletRequest request,
                                      @PathVariable Long userId){
        UserInfoResponse data = memberService.userInfo(request, userId);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_USER_INFO));
    }

    @PostMapping("/{userId}/check-password")
    public ResponseEntity<?> checkPassword(HttpServletRequest servletRequest,
                                           @RequestBody CheckPasswordRequest passwordRequest){
        memberService.checkPassword(servletRequest, passwordRequest);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_CHECK_PASSWORD));
    }

    @PatchMapping("/{userId}/modify")
    public ResponseEntity<?> modifyUserInfo(HttpServletRequest servletRequest,
                                            @RequestBody ModifyUserInfoRequest infoRequest){
        memberService.modifyUserInfo(servletRequest, infoRequest);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_MODIFY_USER_INFO));
    }

    @DeleteMapping("/{userId}/withdraw")
    public ResponseEntity<?> withdrawUser(HttpServletRequest request){
        memberService.withdrawUser(request);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_WITHDRAWAL));
    }

    @GetMapping("/public/posts")
    public ResponseEntity<CommonResponse<LoadPublicPostsResponse>> loadPublicPosts(
            Authentication authentication,
            @RequestParam("page") int page,
            @RequestParam("size") int size)
    {
        Page<Post> posts = memberService.loadPublicPosts(authentication, page, size);
        LoadPublicPostsResponse data = LoadPublicPostsResponse.fromPage(posts);

        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_POST));
    }

    @PostMapping("/modify-password")
    public ResponseEntity<?> modifyPassword(HttpServletRequest servletRequest,
                                            @Valid @RequestBody ModifyPasswordRequest passwordRequest,
                                            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationUtils.getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.failure(errorMessage));
        }

        memberService.modifyPassword(servletRequest, passwordRequest);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_MODIFY_USER_PASSWORD));
    }
}
