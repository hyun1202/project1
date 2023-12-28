package com.hyun.jobty.global.response.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.global.response.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {

    @Override
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    @Override
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setData("list", list);
        setSuccessResult(result);
        return result;
    }

    @Override
    public <T> ListResult<T> getListResult(HashMap<String, T> datas) {
        ListResult<T> result = new ListResult<>();
        result.setData(datas);
        setSuccessResult(result);
        return result;
    }

    @Override
    public <T> ListResult<T> getListResult(Object... data){
        ListResult<T> result = new ListResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    @Override
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    @Override
    public CommonResult getFailResult() {
        CommonResult result = new CommonResult();
        setFailResult(result);
        return result;
    }

    @Override
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        setFailResult(result, code, msg);
        return result;
    }

    @Override
    public CommonResult getFailResult(ErrorCode errorCode) {
        CommonResult result = new CommonResult();
        setFailResult(result, errorCode);
        return result;
    }

    @Override
    public HttpServletResponse setResponseError(HttpServletResponse response, int status, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(CommonResult.builder()
                .success(false)
                .code(status)
                .msg(errorCode.getMsg())
                .build()));
        return response;
    }

    // API 요청 성공 시 응답 모델을 성공 데이터로 세팅
    private void setSuccessResult(CommonResult result){
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    // API 요청 실패 시 응답 모델을 실패 데이터로 세팅
    private void setFailResult(CommonResult result){
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }

    // API 요청 실패 시 응답 모델을 실패 데이터로 세팅
    private void setFailResult(CommonResult result, ErrorCode errorCode){
        result.setSuccess(false);
        result.setCode(errorCode.getCode());
        result.setMsg(errorCode.getMsg());
    }

    private void setFailResult(CommonResult result, int code, String msg){
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
    }
}
