package com.btb.chalKak.global.response.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {

    private boolean success;
    private String message;
    private T data;

}
