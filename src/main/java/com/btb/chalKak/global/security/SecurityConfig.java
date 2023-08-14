package com.btb.chalKak.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS
        http.cors().configurationSource(configurationSource());

        // JWT CSRF 공격에 대해 내재적으로 안전, 토큰 자체에 인증 정보를 포함 -> CSRF 토큰을 검증 필요 X
        http.csrf().disable();

        // JWT 사용으로 비활성화(요청마다 id, pw 보내는 것 비활성화)
        http.httpBasic().disable();

        // iframe 차단
        http.headers().frameOptions().sameOrigin();

        // 시큐리티 기본 로그인 사용 비활성화
        http.formLogin().disable();

        // 세션을 생성하지 않고, 요청마다 독립적인 인증을 수행, 서버가 클라이언트의 상태를 유지하지 않고 JWT 토큰을 통해 클라이언트의 인증
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 권한 설정
        http.authorizeRequests()
                .antMatchers("/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
