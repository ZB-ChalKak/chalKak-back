package com.btb.chalKak.common.security.jwt;

import static com.btb.chalKak.common.response.type.ErrorCode.EXPIRED_JWT_EXCEPTION;
import static com.btb.chalKak.common.response.type.ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION;
import static com.btb.chalKak.common.response.type.ErrorCode.MALFORMED_JWT_EXCEPTION;
import static com.btb.chalKak.common.response.type.ErrorCode.SIGNATURE_EXCEPTION;
import static com.btb.chalKak.common.response.type.ErrorCode.UNSUPPORTED_JWT_EXCEPTION;

import com.btb.chalKak.common.exception.JwtException;
import com.btb.chalKak.common.response.dto.CommonResponse;
import com.btb.chalKak.common.response.type.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            if (Objects.equals(e.getCode(), EXPIRED_JWT_EXCEPTION.name())) {
                sendErrorResponse(response, EXPIRED_JWT_EXCEPTION);
            }
            else if (Objects.equals(e.getCode(), UNSUPPORTED_JWT_EXCEPTION.name())) {
                sendErrorResponse(response, UNSUPPORTED_JWT_EXCEPTION);
            }
            else if (Objects.equals(e.getCode(), MALFORMED_JWT_EXCEPTION.name())) {
                sendErrorResponse(response, MALFORMED_JWT_EXCEPTION);
            }
            else if (Objects.equals(e.getCode(), SIGNATURE_EXCEPTION.name())) {
                sendErrorResponse(response, SIGNATURE_EXCEPTION);
            }
            else if (Objects.equals(e.getCode(), ILLEGAL_ARGUMENT_EXCEPTION.name())) {
                sendErrorResponse(response, ILLEGAL_ARGUMENT_EXCEPTION);
            }
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws RuntimeException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(getErrorJsonByErrorCode(errorCode));
    }

    private String getErrorJsonByErrorCode(ErrorCode errorCode) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                CommonResponse.builder()
                        .success(false)
                        .message(errorCode.getMessage())
                        .build());
    }

}
