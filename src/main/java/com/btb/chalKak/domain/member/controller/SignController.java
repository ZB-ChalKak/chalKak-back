package com.btb.chalKak.domain.member.controller;

import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.request.TokenRequestDto;
import com.btb.chalKak.common.util.ValidationUtils;
import com.btb.chalKak.domain.member.dto.request.CheckPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.dto.response.UserDetailsInfoResponse;
import com.btb.chalKak.domain.member.dto.response.UserInfoResponse;
import com.btb.chalKak.domain.member.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        memberService.validateEmail(email);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_VALIDATE_EMAIL));
    }

    @GetMapping("/validate/nickname/{nickname}")
    public ResponseEntity<?> validateNickname(@PathVariable String nickname){
        memberService.validateNickname(nickname);
        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_VALIDATE_NICKNAME));
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

}
