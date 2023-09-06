package com.btb.chalKak.domain.member.service;

import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.request.TokenRequestDto;
import com.btb.chalKak.domain.member.dto.request.*;
import com.btb.chalKak.domain.member.dto.response.*;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

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

    CheckPasswordResponse checkPassword(HttpServletRequest servletRequest, CheckPasswordRequest passwordRequest);

    void modifyUserInfo(HttpServletRequest servletRequest, Long userId, MultipartFile[] multipartFiles, ModifyUserInfoRequest infoRequest);

    void withdrawUser(HttpServletRequest request);

    Page<Post> loadPublicPosts(Authentication authentication, int page, int size);

    void modifyPassword(HttpServletRequest servletRequest, ModifyPasswordRequest passwordRequest);
}
