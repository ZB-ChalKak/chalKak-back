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
import com.btb.chalKak.domain.post.entity.Post;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

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

    Page<Post> loadPublicPosts(Authentication authentication, int page, int size);
}
