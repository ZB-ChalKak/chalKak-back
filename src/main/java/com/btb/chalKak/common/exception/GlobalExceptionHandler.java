package com.btb.chalKak.common.exception;

import static com.btb.chalKak.common.response.type.ErrorCode.CONSTRAINT_VIOLATION;
import static com.btb.chalKak.common.response.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.btb.chalKak.common.response.type.ErrorCode.INVALID_REQUEST;

import com.btb.chalKak.common.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseService responseService;

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<?> handleMemberException(MemberException e) {
        log.error("MemberException is occurred. {}",  e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(responseService.failure(e.getMessage()));
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<?> handlePostException(PostException e) {
        log.error("PostException is occurred. {}",  e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(responseService.failure(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException is occurred.", e);
        return ResponseEntity.status(INVALID_REQUEST.getHttpStatus()).body(responseService.failure(INVALID_REQUEST));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        log.error("DataIntegrityViolationException is occurred.", e);
        return ResponseEntity.status(CONSTRAINT_VIOLATION.getHttpStatus()).body(responseService.failure(CONSTRAINT_VIOLATION));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){
        log.error("Exception is occurred.", e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR.getHttpStatus()).body(responseService.failure(INTERNAL_SERVER_ERROR));
    }
}
