package com.jobty.conf.security.handler;

import com.jobty.advice.exception.ErrorCode;
import com.jobty.global.response.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    private final ResponseService responseService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("401 forbidden, {}", request.getRequestURI());
        responseService.setResponseError(response, HttpStatus.FORBIDDEN.value(), ErrorCode.UnrecognizedRole);
//        throw new CustomException(ErrorCode.UnrecognizedRole);
    }
}
