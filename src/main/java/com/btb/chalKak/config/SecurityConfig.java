package com.btb.chalKak.config;

import com.btb.chalKak.common.oauth2.handler.OAuth2SuccessHandler;
import com.btb.chalKak.common.oauth2.service.CustomOAuth2UserService;
import com.btb.chalKak.common.security.jwt.JwtAccessDeniedHandler;
import com.btb.chalKak.common.security.jwt.JwtAuthenticationEntryPoint;
import com.btb.chalKak.common.security.jwt.JwtAuthenticationFilter;
import com.btb.chalKak.common.security.jwt.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

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
                .authenticated()
            .and()
                .logout()
                .logoutSuccessUrl("/") // 로그 아웃 성공 시 / 주소로 이동
            .and()
                .oauth2Login()
                .defaultSuccessUrl("/")
//                .failureUrl("/")
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService) // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 구현체
            ;

        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 예외 핸들링
                .accessDeniedHandler(jwtAccessDeniedHandler); // 인가 예외 핸들링

        // JWT 인증 필터 적용
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

}
