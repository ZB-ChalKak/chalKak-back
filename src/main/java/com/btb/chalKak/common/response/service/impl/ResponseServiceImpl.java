package com.btb.chalKak.common.response.service.impl;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.common.response.type.ErrorCode;
import com.btb.chalKak.common.response.type.SuccessCode;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {

    @Override
    public CommonResponse<?> successWithNoContent(SuccessCode successCode) {
        return CommonResponse.builder()
                .success(true)
                .message(successCode.getMessage())
                .data(null)
                .build();
    }

    @Override
    public <T> CommonResponse<T> success(T data, SuccessCode successCode) {
        return CommonResponse.<T>builder()
                .success(true)
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    @Override
    public <T> CommonResponse<List<T>> successList(List<T> data, SuccessCode successCode) {
        return CommonResponse.<List<T>>builder()
                .success(true)
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    @Override
    public <T> CommonResponse<T> failure(ErrorCode errorCode) {
        return CommonResponse.<T>builder()
                .success(false)
                .message(errorCode.getMessage())
                .build();
    }

    @Override
    public <T> CommonResponse<T> failure(String errorMessage) {
        return CommonResponse.<T>builder()
                .success(false)
                .message(errorMessage)
                .build();
    }
}
