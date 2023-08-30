package com.btb.chalKak.common.response.service;

import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.exception.type.ErrorCode;
import com.btb.chalKak.common.exception.type.SuccessCode;
import java.util.List;

public interface ResponseService {

    // 데이터가 존재하지 않는 성공 응답
    CommonResponse<?> successWithNoContent(SuccessCode successCode);

    // 데이터가 존재하는 성공 응답
    <T> CommonResponse<T> success(T data, SuccessCode successCode);

    // 리스트 데이터가 존재하는 성공 응답
    <T> CommonResponse<List<T>> successList(List<T> data, SuccessCode successCode);

    // 실패 응답 (ErrorCode)
    <T> CommonResponse<T> failure(ErrorCode errorCode);

    // 실패 응답 (ErrorMessage)
    <T> CommonResponse<T> failure(String errorMessage);

}
