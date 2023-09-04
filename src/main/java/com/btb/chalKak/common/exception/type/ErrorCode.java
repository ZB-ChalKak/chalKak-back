package com.btb.chalKak.common.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    LOAD_MEMBER_FAILED(HttpStatus.UNAUTHORIZED, "회원 정보를 불러오는데 실패했습니다."),

    CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "제약 조건 위반"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생하였습니다."),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // TOKEN
    UNAUTHORIZED_RESPONSE(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    FORBIDDEN_RESPONSE(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    MALFORMED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 형식의 토큰입니다."),
    SIGNATURE_EXCEPTION(HttpStatus.BAD_REQUEST, "토큰 서명이 올바르지 않습니다."),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 인자가 전달되었습니다."),

    // MEMBER
    ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    ALREADY_EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),

    INVALID_MEMBER_ID(HttpStatus.BAD_REQUEST, "잘못된 회원 번호입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "잘못된 형식의 닉네임입니다."),

    INACTIVE_MEMBER(HttpStatus.UNAUTHORIZED, "비활성화된 사용자입니다."),
    BLOCKED_MEMBER(HttpStatus.UNAUTHORIZED, "정지된 사용자입니다."),
    WITHDRAWAL_MEMBER(HttpStatus.UNAUTHORIZED, "탈퇴한 사용자입니다."),

    INACTIVE_SING_IN(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // POST
    INVALID_POST_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글 번호입니다."),
    MISMATCH_WRITER(HttpStatus.BAD_REQUEST, "작성자가 아닙니다."),

    // COMMENT
    INVALID_COMMENT_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 댓글 번호입니다."),

    // LIKE
    NOT_FOUND_LIKE_ID(HttpStatus.BAD_REQUEST, "좋아요 정보를 찾을 수 없습니다"),
    ALREADY_LIKE(HttpStatus.BAD_REQUEST, "이미 좋아요가 등록되었습니다."),

    // FOLLOW
    NOT_FOUND_FOLLOW_ID(HttpStatus.BAD_REQUEST, "팔로우 정보를 찾을 수 없습니다"),

    //소셜 로그인

    INVALID_EMAIL_LOGIN(HttpStatus.BAD_REQUEST, "CHALKAK 계정이 아닙니다."),

    // Filter
    INVALID_PREVIEW_CONTENT_LENGTH(HttpStatus.BAD_REQUEST, "올바른 문자열 길이가 아닙니다."),
    ;



    private final HttpStatus httpStatus;
    private final String message;

}
