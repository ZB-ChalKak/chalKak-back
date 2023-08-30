package com.btb.chalKak.common.exception;

import static com.btb.chalKak.common.exception.type.ErrorCode.CONSTRAINT_VIOLATION;
import static com.btb.chalKak.common.exception.type.ErrorCode.FORBIDDEN_RESPONSE;
import static com.btb.chalKak.common.exception.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_REQUEST;
import static com.btb.chalKak.common.exception.type.ErrorCode.UNAUTHORIZED_RESPONSE;

import com.btb.chalKak.common.response.service.ResponseService;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<?> handleCommentException(PostException e) {
        log.error("CommentException is occurred. {}",  e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(responseService.failure(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        log.error("AuthenticationException is occurred. {}",  e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED_RESPONSE.getHttpStatus()).body(responseService.failure(UNAUTHORIZED_RESPONSE.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException is occurred. {}",  e.getMessage());
        return ResponseEntity.status(FORBIDDEN_RESPONSE.getHttpStatus()).body(responseService.failure(FORBIDDEN_RESPONSE.getMessage()));
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
