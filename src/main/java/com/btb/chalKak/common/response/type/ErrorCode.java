package com.btb.chalKak.common.response.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    LOAD_USER_FAILED(HttpStatus.UNAUTHORIZED, "회원 정보를 불러오는데 실패했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
