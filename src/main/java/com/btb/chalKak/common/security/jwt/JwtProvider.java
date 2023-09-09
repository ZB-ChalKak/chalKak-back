package com.btb.chalKak.common.security.jwt;

import static com.btb.chalKak.common.exception.type.ErrorCode.EXPIRED_JWT_EXCEPTION;
import static com.btb.chalKak.common.exception.type.ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION;
import static com.btb.chalKak.common.exception.type.ErrorCode.MALFORMED_JWT_EXCEPTION;
import static com.btb.chalKak.common.exception.type.ErrorCode.SIGNATURE_EXCEPTION;
import static com.btb.chalKak.common.exception.type.ErrorCode.UNSUPPORTED_JWT_EXCEPTION;

import com.btb.chalKak.common.exception.JwtException;
import com.btb.chalKak.common.security.customUser.CustomUserDetailsService;
import com.btb.chalKak.common.security.dto.TokenReissueResponse;
import com.btb.chalKak.domain.member.type.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String KEY_ROLE = "role";

    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 3 * 60 * 60 * 1000L; // 3 hour
    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7 day

    private final CustomUserDetailsService userDetailsService;

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    protected void init(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenReissueResponse createToken(String email, MemberRole role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLE, role);

        Date now = new Date();

        long accessTokenExpireTime = now.getTime() + ACCESS_TOKEN_EXPIRE_TIME;
        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(accessTokenExpireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenReissueResponse.builder()
                .grantType(TOKEN_PREFIX.substring(0, TOKEN_PREFIX.length() - 1))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenExpireTime)
                .build();
    }

    // 토큰 추출
    public String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        // token 값이 빈 값일 경우 유효하지 않다.
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    // JWT 토큰 복호화
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException(EXPIRED_JWT_EXCEPTION);
        }  catch (UnsupportedJwtException e) {
            throw new JwtException(UNSUPPORTED_JWT_EXCEPTION);
        } catch (MalformedJwtException e) {
            throw new JwtException(MALFORMED_JWT_EXCEPTION);
        } catch (SignatureException e) {
            throw new JwtException(SIGNATURE_EXCEPTION);
        } catch (IllegalArgumentException e) {
            throw new JwtException(ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

    // Security 권한 확인
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getSubjectByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Subject(email) 가져오기
    public String getSubjectByToken(String token){
        return parseClaims(token).getSubject();
    }

}
