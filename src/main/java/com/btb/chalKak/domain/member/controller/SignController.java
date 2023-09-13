package com.btb.chalKak.domain.member.controller;

import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_CHECK_PASSWORD;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_CONFIRM_AUTH;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_POST;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_USER_DETAILS_INFO;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_USER_INFO;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_VALIDATE_EMAIL;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_LOAD_VALIDATE_NICKNAME;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_MODIFY_USER_INFO;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_MODIFY_USER_PASSWORD;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_REISSUE;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_SAVE_MEMBER;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_SIGN_IN;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_SIGN_OUT;
import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_WITHDRAWAL;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.common.security.dto.TokenReissueResponse;
import com.btb.chalKak.common.security.request.TokenReissueRequest;
import com.btb.chalKak.common.util.ValidationUtils;
import com.btb.chalKak.domain.member.dto.request.CheckPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.ConfirmAuthRequest;
import com.btb.chalKak.domain.member.dto.request.ModifyPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.ModifyUserInfoRequest;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.CheckPasswordResponse;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.dto.response.UserDetailsInfoResponse;
import com.btb.chalKak.domain.member.dto.response.UserInfoResponse;
import com.btb.chalKak.domain.member.dto.response.ValidateInfoResponse;
import com.btb.chalKak.domain.member.service.MemberService;
import com.btb.chalKak.domain.post.dto.response.LoadUserPublicPostsResponse;
import com.btb.chalKak.domain.post.entity.Post;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> reissue(@RequestBody TokenReissueRequest tokenReissueRequest){
        TokenReissueResponse data = memberService.reissue(tokenReissueRequest);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_REISSUE));
    }

    @PutMapping("/signout")
    public ResponseEntity<?> signOut(Authentication authentication) {
        memberService.signOut(authentication);
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
    public ResponseEntity<?> userInfo(Authentication authentication,
                                      @PathVariable Long userId){
        UserInfoResponse data = memberService.userInfo(authentication, userId);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_USER_INFO));
    }

    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(Authentication authentication,
                                           @RequestBody CheckPasswordRequest passwordRequest){
        CheckPasswordResponse data = memberService.checkPassword(authentication, passwordRequest);
        return ResponseEntity.ok(responseService.success(data, SUCCESS_CHECK_PASSWORD));
    }

    @PatchMapping(value = "/{userId}/modify", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> modifyUserInfo(Authentication authentication,
                                            @PathVariable Long userId,
                                            @RequestPart(required = false) MultipartFile[] multipartFiles,
                                            @RequestPart ModifyUserInfoRequest infoRequest){
        memberService.modifyUserInfo(authentication, userId, multipartFiles, infoRequest);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_MODIFY_USER_INFO));
    }

    @DeleteMapping("/{userId}/withdraw")
    public ResponseEntity<?> withdrawUser(Authentication authentication){
        memberService.withdrawUser(authentication);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_WITHDRAWAL));
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<CommonResponse<LoadUserPublicPostsResponse>> loadPublicPosts(
            Authentication authentication,
            @PathVariable Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size)
    {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = memberService.loadPublicPosts(authentication, userId, pageRequest);
        LoadUserPublicPostsResponse data = LoadUserPublicPostsResponse.fromPage(posts);

        if (memberService.validateMemberId(authentication, userId)) {
            data.updateAuthenticated();
        }

        return ResponseEntity.ok(responseService.success(data, SUCCESS_LOAD_POST));
    }

    @PostMapping("/modify-password")
    public ResponseEntity<?> modifyPassword(Authentication authentication,
                                            @Valid @RequestBody ModifyPasswordRequest passwordRequest,
                                            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationUtils.getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.failure(errorMessage));
        }

        memberService.modifyPassword(authentication, passwordRequest);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_MODIFY_USER_PASSWORD));
    }

    @PostMapping("/auth")
    public ResponseEntity<CommonResponse<?>> authConfirm(
            @RequestBody ConfirmAuthRequest confirmAuthRequest)
    {
        memberService.confirmAuth(confirmAuthRequest.getId(), confirmAuthRequest.getAuthToken());
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_CONFIRM_AUTH));
    }
}
