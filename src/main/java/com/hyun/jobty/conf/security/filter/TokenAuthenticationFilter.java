package com.hyun.jobty.conf.security.filter;

import com.hyun.jobty.conf.security.jwt.TokenProvider;
import com.hyun.jobty.domain.member.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    private String getAccessToken(String authorizationHeader){
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)){
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 요청 헤더의 Authorization 키 값 조회
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorizationHeader = httpServletRequest.getHeader(HEADER_AUTHORIZATION);

        // 가져온 값의 접두사 제거
        String token = getAccessToken(authorizationHeader);
        // 토큰 검증 및 인증 정보 설정
        log.debug("token: {}", token);
        if (tokenProvider.validToken(token)) {
            // 토큰 db와 검사
            log.debug("token_id: {}", tokenProvider.getMemberId(token));
            String tokenId = tokenProvider.getMemberId(token);
            if (tokenId != null) {
                if (tokenService.validJwtToken(tokenId, token)) {
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
