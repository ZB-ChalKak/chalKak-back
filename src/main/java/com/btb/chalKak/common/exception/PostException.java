package com.btb.chalKak.common.exception;

import com.btb.chalKak.common.response.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String message;

    public PostException(ErrorCode errorCode) {
        this.httpStatus = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }
}
