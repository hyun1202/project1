package com.hyun.jobty.global.response.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.global.response.*;
import com.hyun.jobty.util.file.FileUtil;
import com.hyun.jobty.util.file.FileVo;
import com.hyun.jobty.util.Util;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {

    private <T> ResponseEntity<T> getResponseEntity(T data){
        return Util.responseEntityOf(data);
    }

    public ResponseEntity<Resource> getFileResponseEntity(FileVo file){
        HttpHeaders headers = new HttpHeaders();
        String oriFileName = "";
        if (file.oriFileName().equals("") || file.oriFileName() == null){
            oriFileName = Util.random() + "." + Util.getFileExtension(file.saveFilePath());
        }
        String encodedOriginalFileName = UriUtils.encode(oriFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        UrlResource resource;
        try{
            resource = new FileUtil().downloadFile(file);
            return Util.responseEntityOf(resource, headers);
        }catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL);
        }
    }

    @Override
    public <T> ResponseEntity<SingleResult<T>> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return this.getResponseEntity(result);
    }

    @Override
    public <T> ResponseEntity<ListResult<T>> getListResult(List<T> data){
        ListResult<T> result = new ListResult<>();
        result.setData(data);
        setSuccessResult(result);
        return this.getResponseEntity(result);
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
    public CommonResult getFailResult(CommonReason commonReason) {
        CommonResult result = new CommonResult();
        setFailResult(result, commonReason);
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
        result.setCode(CommonCode.SUCCESS.getCode());
        result.setMsg(CommonCode.SUCCESS.getMsg());
    }

    // API 요청 실패 시 응답 모델을 실패 데이터로 세팅
    private void setFailResult(CommonResult result){
        result.setSuccess(false);
        result.setCode(CommonCode.FAIL.getCode());
        result.setMsg(CommonCode.FAIL.getMsg());
    }

    // API 요청 실패 시 응답 모델을 실패 데이터로 세팅
    private void setFailResult(CommonResult result, CommonReason commonReason){
        result.setSuccess(false);
        result.setCode(commonReason.getCode());
        result.setMsg(commonReason.getMsg());
    }

    private void setFailResult(CommonResult result, int code, String msg){
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
    }
}
