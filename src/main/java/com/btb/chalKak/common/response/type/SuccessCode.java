package com.btb.chalKak.common.response.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    SUCCESS("성공했습니다."),
    SUCCESS_SAVE_POST("게시글 등록에 성공했습니다."),
    SUCCESS_LOAD_POST("게시글 조회에 성공했습니다."),
    SUCCESS_DELETE_POST("게시글 삭제에 성공했습니다."),

    // weather

    SUCCESS_LOAD_WEATHER("날씨 조회에 성공했습니다.")
    ;



    private final String message;

}
