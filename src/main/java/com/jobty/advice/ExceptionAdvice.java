package com.jobty.advice;

import com.jobty.advice.exception.CustomException;
import com.jobty.advice.exception.ErrorCode;
import com.jobty.global.response.CommonResult;
import com.jobty.global.response.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e){
        e.printStackTrace();
        return responseService.getFailResult();
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected  CommonResult HttpRequestMethodNotSupportedException(HttpServletRequest request, Exception e){
        log.info("{}에 해당하는 http method가 없습니다.(url 확인)", request.getRequestURI());
        return responseService.getFailResult(-1, "해당하는 URL이 없습니다.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult httpRequestMessageException(HttpServletRequest request, Exception e){
        return responseService.getFailResult(-1, "요청 값이 올바르지 않습니다.");
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult FileNotFoundException(HttpServletRequest request, Exception e){
        return responseService.getFailResult(-1, "해당하는 파일이 없습니다.");
    }


    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.OK)
    protected CommonResult customException(HttpServletRequest request, CustomException e){
        return responseService.getFailResult(e.getErrorCode().getCommonReason());
    }

    /**
     * validation 예외 처리
     * not null 필드일 경우 not null 메시지를 붙여서 리턴한다.
     * @param exception advice 파라미터
     * @return 입력 값 검증 오류 내용
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        StringBuilder builder = new StringBuilder();
        String msg = ErrorCode.FAIL.getMsg();

        String not_null_msg = "(은)는 필수 값입니다.";
        List<String> requiredFields = new ArrayList<>();

        // Not Null 체크
        // 필드 확인
        for (Field field : exception.getParameter().getParameterType().getDeclaredFields()){
            field.setAccessible(true);
            for (Annotation annotation : field.getAnnotations()){
                if (annotation.annotationType() == NotNull.class){
                    requiredFields.add(field.getName());
                    break;
                }
            }
        }

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String fieldName = fieldError.getField();

            if (!fieldError.getField().equals("multipartFiles")) {
                msg = fieldError.getDefaultMessage();
            }

            // Not null 필드 확인
            if (fieldError.getDefaultMessage().equals("널이어서는 안됩니다")) {
                for (String requiredField : requiredFields) {
                    if (fieldError.getField().equals(requiredField)) {
                        msg = "[" + fieldName + "]" + not_null_msg;
                        break;
                    }
                }
            }

            builder.append("[");
            builder.append(fieldName);
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("] \n");
            builder.append("custom msg: ");
            builder.append("[");
            builder.append(msg);
            builder.append("]");
            builder.append(msg);

        }
        log.debug(builder.toString());

        return responseService.getFailResult(-1, msg);
    }
}
