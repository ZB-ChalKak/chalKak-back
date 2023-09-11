package com.btb.chalKak.domain.member.service;

import com.btb.chalKak.common.security.dto.TokenReissueResponse;
import com.btb.chalKak.common.security.request.TokenReissueRequest;
import com.btb.chalKak.domain.emailAuth.entity.EmailAuth;
import com.btb.chalKak.domain.member.dto.request.CheckPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.ModifyPasswordRequest;
import com.btb.chalKak.domain.member.dto.request.ModifyUserInfoRequest;
import com.btb.chalKak.domain.member.dto.request.SignInMemberRequest;
import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.dto.response.CheckPasswordResponse;
import com.btb.chalKak.domain.member.dto.response.SignInMemberResponse;
import com.btb.chalKak.domain.member.dto.response.UserDetailsInfoResponse;
import com.btb.chalKak.domain.member.dto.response.UserInfoResponse;
import com.btb.chalKak.domain.member.dto.response.ValidateInfoResponse;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    // 회원 가입
    void signUp(SignUpMemberRequest request);

    SignInMemberResponse SignIn(SignInMemberRequest request);

    TokenReissueResponse reissue(TokenReissueRequest tokenReissueRequest);

    Member getMemberByAuthentication(Authentication authentication);

    boolean validateMemberId(Authentication authentication, Long memberId);

    void signOut(Authentication authentication);

    ValidateInfoResponse validateEmail(String email);

    ValidateInfoResponse validateNickname(String nickname);

    UserDetailsInfoResponse userDetailsInfo(Long userId);

    UserInfoResponse userInfo(Authentication authentication, Long userId);

    CheckPasswordResponse checkPassword(Authentication authentication, CheckPasswordRequest passwordRequest);

    void modifyUserInfo(Authentication authentication, Long userId, MultipartFile[] multipartFiles, ModifyUserInfoRequest infoRequest);

    void withdrawUser(Authentication authentication);

    Page<Post> loadPublicPosts(Authentication authentication, Long memberId, int page, int size);

    void modifyPassword(Authentication authentication, ModifyPasswordRequest passwordRequest);

    void sendConfirmEmail(Member member, EmailAuth emailAuth);

    void confirmAuth(Long id, String authToken);

}
