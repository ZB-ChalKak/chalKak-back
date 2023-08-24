package com.btb.chalKak.common.exception;

import com.btb.chalKak.common.response.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberException extends RuntimeException{

    private HttpStatus httpStatus;
    private String message;

    public MemberException(ErrorCode errorCode) {
        this.httpStatus = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }
}
