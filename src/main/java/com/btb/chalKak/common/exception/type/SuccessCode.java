package com.btb.chalKak.common.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    SUCCESS("성공했습니다."),
    SUCCESS_WRITE_POST("게시글 등록에 성공했습니다."),
    SUCCESS_EDIT_POST("게시글 수정에 성공했습니다."),
    SUCCESS_LOAD_POST("게시글 조회에 성공했습니다."),
    SUCCESS_DELETE_POST("게시글 삭제에 성공했습니다."),

    SUCCESS_SAVE_MEMBER("회원 가입에 성공했습니다."),
    SUCCESS_SIGN_IN("로그인에 성공했습니다."),
    SUCCESS_REISSUE("토큰 재발급 성공"),
    SUCCESS_LOAD_WEATHER("날씨 조회에 성공했습니다."),

    // 댓글
    SUCCESS_SAVE_COMMENT("댓글 등록에 성공했습니다."),
    SUCCESS_LOAD_COMMENT("댓글 조회에 성공했습니다."),
    SUCCESS_MODIFY_COMMENT("댓글 수정 성공했습니다."),
    SUCCESS_DELETE_COMMENT("댓글 삭제에 성공했습니다."),

    // 좋아요
    SUCCESS_LIKE("좋아요 등록에 성공했습니다."),
    SUCCESS_UNLIKE("좋아요 삭제에 성공했습니다."),

    // 팔로우
    SUCCESS_FOLLOW("팔로우 등록에 성공했습니다."),
    SUCCESS_UNFOLLOW("팔로우 취소에 성공했습니다."),

    // 스타일 태그
    SUCCESS_LOAD_STYLE_TAG("스타일 태그 조회에 성공했습니다."),

    ;

    private final String message;

}
