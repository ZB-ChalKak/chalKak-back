package com.btb.chalKak.domain.member.controller;

import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.common.response.type.SuccessCode;
import com.btb.chalKak.common.security.JwtTokenProvider;
import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.request.TokenRequestDto;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.service.MemberService;
import com.btb.chalKak.domain.member.type.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.btb.chalKak.common.response.type.SuccessCode.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class SignController {

    private final MemberService memberService;
    private final ResponseService responseService;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpMemberRequest request){
        memberService.saveMember(request);

        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_SAVE_MEMBER));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInMemberRequest request){
        SignInMemberResponse data = memberService.logIn(request);

        return ResponseEntity.ok(responseService.success(data, SUCCESS_SIGN_IN));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        TokenDto data = memberService.reissue(tokenRequestDto);

        return ResponseEntity.ok(responseService.success(data, SUCCESS_REISSUE));
    }
}
