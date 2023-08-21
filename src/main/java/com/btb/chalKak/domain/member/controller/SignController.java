package com.btb.chalKak.domain.member.controller;

import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.common.response.type.SuccessCode;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.btb.chalKak.common.response.type.SuccessCode.SUCCESS_SAVE_MEMBER;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class SignController {

    private final MemberService memberService;
    private final ResponseService responseService;

    @PostMapping("/signup")
    public ResponseEntity<?> saveMember(@RequestBody SignUpMemberRequest request){
        Member member = memberService.saveMember(request);

        return ResponseEntity.ok(responseService.successWithNoContent(SUCCESS_SAVE_MEMBER));
    }

}
