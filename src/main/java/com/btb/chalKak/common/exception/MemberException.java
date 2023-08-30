package com.btb.chalKak.common.exception;

import com.btb.chalKak.common.exception.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public MemberException(ErrorCode errorCode) {
        this.httpStatus = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }
}
