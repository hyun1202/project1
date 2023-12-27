package com.hyun.jobty.global.response;

import com.hyun.jobty.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
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
     * 단건 데이터 처리
     * @param data 단건 데이터
     * @return 공통 코드, 메시지가 포함된 데이터 리턴
     */
    <T> SingleResult<T> getSingleResult(T data);

    /**
     * 다건 데이터(리스트) 처리
     * @param list 다건 데이터
     * @return 공통 코드, 메시지가 포함된 데이터 리턴
     */
    <T> ListResult<T> getListResult(List<T> list);

    /**
     * 다건 데이터(HashMap) 처리
     * @param datas 다건 데이터
     * @return 공통 코드, 메시지가 포함된 데이터 리턴
     */
    <T> ListResult<T> getListResult(HashMap<String, T> datas);

    /**
     * 다건 List 데이터 (HashMap) 처리
     * @param data String, Object순으로 데이터 설정
     * @param <T>
     * @return ListResult 데이터
     */
    <T> ListResult<T> getListResult(Object... data);

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

    /**
     * 에러코드를 지정하여 실패 데이터 처리
     * <p>{@link com.hyun.jobty.global.advice.ExceptionAdvice}에서 예외 처리를 위해 이용</p>
     * @param errorCode 에러코드
     * @return 에러코드에 해당하는 실패 데이터 리턴
     */
    CommonResult getFailResult(ErrorCode errorCode);
    HttpServletResponse setResponseError(HttpServletResponse response, int status, ErrorCode errorCode) throws IOException;
}
