package com.hyun.jobty.global.aop;

import com.hyun.jobty.global.annotation.FileUpload;
import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.global.util.FileRequest;
import com.hyun.jobty.global.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FileAspect {
    private FileUtil fileUtil;

    public FileAspect(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Pointcut("@annotation(com.hyun.jobty.global.annotation.FileUpload)")
    private void fileUpload() {}
    @Pointcut("@annotation(com.hyun.jobty.global.annotation.FileDownload)")
    private void fileDownload() {}

    /**
     * {@link FileUpload} 어노테이션이 있으면 서버에 파일업로드 실행
     * 컨트롤러에서 파일을 받을 때 {@link FileRequest} 상속 또는 해당 객체로 받아야한다.
     * <pre> 업로드 한 파일 정보 가져오기
     *     List<FileVo> files = fileUtil.getFileInfo();
     *     for (FileVo file : files) {
     *          file.oriFileName();
     *          file.saveFileName();
     *          file.saveFilePath();
     *    }
     * </pre>
     */
    @Around("fileUpload()")
    public Object fileUpload(ProceedingJoinPoint joinPoint) throws Throwable{
        // 파일 파라미터명 확인, FileRequest 상속 받아 사용
        String fileParam = "multipartFiles";
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        FileUpload fileUpload = signature.getMethod().getAnnotation(FileUpload.class);
        String pathId = fileUpload.path();
        String idParam = fileUpload.idParam();
        boolean idUsage = fileUpload.idUsage();
        String memberId = "";
        if (getIndex(paramNames, idParam) != -1 && idUsage) {
            memberId = (String) args[getIndex(paramNames, idParam)];
        }

        FileRequest fileRequest = (FileRequest) args[getIndex(paramNames, fileParam)];
        if (!fileUtil.uploadFiles(fileRequest.getMultipartFiles(), memberId, pathId)) {
            throw new CustomException(ErrorCode.FailedSaveFile);
        }
        return joinPoint.proceed(args);
    }

    @Around("fileDownload()")
    public Object fileDownload(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 파라미터명 확인, 기본값은 id
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        return joinPoint.proceed(args);
    }

    private int getIndex(String[] names, String name){
        int index = -1;
        for (int i=0; i< names.length; i++){
            index = i;
            if (name.equals(names[i]))
                return index;
        }
        return index;
    }
}
