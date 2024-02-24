package com.hyun.jobty.global.response;

import com.hyun.jobty.advice.ExceptionAdvice;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.util.file.FileVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * 응답 데이터 처리 클래스
 * <pre> 사용 예제:
 *     private final ResponseService responseService;
 *     responseService.getSingleResult(data);
 *     responseService.getListResult(datas);
 * </pre>
 */
public interface ResponseService {
    /**
     * 파일 다운로드 데이터 처리
     * @param file 파일
     * @return ResponseEntity
     */
    ResponseEntity<Resource> getFileResponseEntity(FileVo file);
    /**
     * 단건 데이터 처리
     * @param data 단건 데이터
     * @return 공통 코드, 메시지가 포함된 데이터 리턴
     */
    <T> ResponseEntity<SingleResult<T>> getSingleResult(T data);

    /**
     * 다건 List 데이터 처리
     * @param <T>
     * @param data List 형식 데이터
     * @return ListResult 데이터
     */
    <T> ResponseEntity<ListResult<T>> getListResult(List<T> data);

    /**
     * 기본 성공 데이터 처리
     * @return 성공 데이터 리턴
     */
    CommonResult getSuccessResult();

    /**
     * 기본 실패 데이터 처리
     * @return 실패 데이터 리턴
     */
    CommonResult getFailResult();

    CommonResult getFailResult(CommonReason commonReason);
    CommonResult getFailResult(int code, String msg);
    /**
     * 에러코드를 지정하여 실패 데이터 처리
     * <p>{@link ExceptionAdvice}에서 예외 처리를 위해 이용</p>
     * @param errorCode 에러코드
     * @return 에러코드에 해당하는 실패 데이터 리턴
     */
    HttpServletResponse setResponseError(HttpServletResponse response, int status, ErrorCode errorCode) throws IOException;
}
