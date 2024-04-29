package com.jobty.conf.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobty.advice.exception.ErrorCode;
import com.jobty.global.response.CommonResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }catch(Exception e){
            e.printStackTrace();
            setErrorResponse(response, ErrorCode.TokenUserNotFound);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        CommonResult commonResult = CommonResult.builder()
                .success(false)
                .code(errorCode.getCode())
                .msg(errorCode.getMsg())
                .build();
        try{
            response.getWriter().write(objectMapper.writeValueAsString(commonResult));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
