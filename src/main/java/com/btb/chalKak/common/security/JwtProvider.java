package com.btb.chalKak.common.security;

import com.btb.chalKak.common.security.dto.TokenDto;
import com.btb.chalKak.common.security.service.Impl.CustomUserDetailsService;
import com.btb.chalKak.domain.member.type.MemberRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
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

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final CustomUserDetailsService userDetailsService;

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private Key key;

    // 토큰 유효 시간은 1시간 (임시)
    private final Long tokenValidMillisecond = 60 * 60 * 1000L;

    // refresh 토큰 유효 시간은 7일 (임시)
    private final Long refreshTokenValidMilliSecond = 60 * 60 * 1000L * 24 * 7;

    // secretKey 초기화
    @PostConstruct
    protected void init(){
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    
    // token 으로 claims 추출
    public Claims getClaimsByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // token 으로 email 추출
    public String getEmailByToken(String token){
        return getClaimsByToken(token)
                .getSubject();
    }
    
    // 토큰 생성
    public TokenDto createToken(String email, MemberRole roles){

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        Date now = new Date();

        Long accessTokenExpireTime = now.getTime() + tokenValidMillisecond;
        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(accessTokenExpireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMilliSecond))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType("baerer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenExpireTime)
                .build();
    }

    // 토큰 인증
    public Authentication getAuthentication(String token) {
        UserDetails user = userDetailsService.loadUserByUsername(this.getEmailByToken(token));

        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }
    
    // 토큰 정보 헤더에서 읽어오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token){
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e){
            // jwtexpiredException or 그냥 false 값만 던져도 filter에서 처리
            return false;
        }
    }
}
