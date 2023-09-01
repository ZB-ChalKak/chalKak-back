package com.btb.chalKak.domain.member.service;

import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.request.TokenRequestDto;
import com.btb.chalKak.domain.member.dto.request.CheckPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.ModifyUserInfoRequest;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.dto.response.UserDetailsInfoResponse;
import com.btb.chalKak.domain.member.dto.response.UserInfoResponse;
import com.btb.chalKak.domain.member.dto.response.ValidateInfoResponse;
import com.btb.chalKak.domain.member.entity.Member;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface MemberService {

    // 회원 가입
    void signUp(SignUpMemberRequest request);

    SignInMemberResponse SignIn(SignInMemberRequest request);

    TokenDto reissue(TokenRequestDto tokenRequestDto);

    Member getMemberByAuthentication(Authentication authentication);

    boolean validateMemberId (Authentication authentication, Long memberId);

    void signOut(HttpServletRequest request);

    ValidateInfoResponse validateEmail(String email);

    ValidateInfoResponse validateNickname(String nickname);

    UserDetailsInfoResponse userDetailsInfo(Long userId);

    UserInfoResponse userInfo(HttpServletRequest request, Long userId);

    void checkPassword(HttpServletRequest servletRequest, CheckPasswordRequest passwordRequest);

    void modifyUserInfo(HttpServletRequest servletRequest, ModifyUserInfoRequest infoRequest);

    void withdrawUser(HttpServletRequest request);
}
