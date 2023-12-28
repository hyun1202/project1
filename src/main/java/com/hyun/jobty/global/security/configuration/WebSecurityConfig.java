package com.hyun.jobty.global.security.configuration;

import com.hyun.jobty.global.security.filter.ExceptionHandlerFilter;
import com.hyun.jobty.global.security.filter.TokenAuthenticationFilter;
import com.hyun.jobty.global.security.handler.AccessDeniedHandler;
import com.hyun.jobty.global.security.handler.AuthenticationEntryPoint;
import com.hyun.jobty.global.security.jwt.TokenProvider;
import com.hyun.jobty.member.service.TokenService;
import com.hyun.jobty.member.service.impl.MemberDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final MemberDetailServiceImpl memberDetailServiceImpl;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    // 시큐리티 ignore 설정
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable()) // csrf 비활성화
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))    //cors 필터 등록
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider, tokenService), UsernamePasswordAuthenticationFilter.class)  //토큰 필터 등록
                .addFilterBefore(new ExceptionHandlerFilter(), TokenAuthenticationFilter.class)
                // 접근 제한 설정
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/login/**", "/signup/**", "/api/token/**", "/checkId/**").permitAll()  // 해당하는 경로는 누구나 접근 가능
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/swagger-resources/**, ").permitAll()
                                .anyRequest().authenticated() // 그 이외 경로는 인가는 필요하지 않으나 인증이 성공되어야 함.
                )
                // 에러 핸들링
                .exceptionHandling((exception) -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                // 세션 사용 X
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin((formLogin) -> formLogin.disable())  // 로그인폼을 사용하지 않으므로 비활성화, 안해주면 파라미터를 uesrname, password로 받고 application/x-www-form-urlencoded 형식으로 받아야함
        ;
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        String[] list = new String[] {"http://localhost:8081"}; //응답할 주소 설정
        config.setAllowCredentials(true);   //내 서버가 응답할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
        config.setAllowedOriginPatterns(Arrays.asList(list));
        config.addAllowedHeader("*");   // 모든 header에 응답 허용
        config.addAllowedMethod("*");   //모든 post, get, put, delete, patch 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(memberDetailServiceImpl);
        // 비밀번호 암호화 후 넘기는 부분
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
