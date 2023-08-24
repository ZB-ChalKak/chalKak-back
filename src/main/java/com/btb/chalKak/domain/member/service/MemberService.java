package com.btb.chalKak.domain.member.service;

import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.request.TokenRequestDto;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;

public interface MemberService {

    void signUp(SignUpMemberRequest request);

    SignInMemberResponse logIn(SignInMemberRequest request);

    TokenDto reissue(TokenRequestDto tokenRequestDto);
}
