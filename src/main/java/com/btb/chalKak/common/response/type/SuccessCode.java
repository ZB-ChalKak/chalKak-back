package com.btb.chalKak.common.response.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    SUCCESS("성공했습니다."),
    SUCCESS_SAVE_POST("게시글 등록에 성공했습니다."),
    SUCCESS_SAVE_MEMBER("회원 가입에 성공했습니다."),
    SUCCESS_SIGN_IN("로그인에 성공했습니다."),
    SUCCESS_REISSUE("토큰 재발급 성공")
    ;

    private final String message;

}
