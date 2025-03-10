package com.jobty.advice;

import com.jobty.global.file.annotation.FileUpload;
import com.jobty.global.file.dto.FileReq;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FileAspect {
    @Pointcut("@annotation(com.jobty.global.file.annotation.FileUpload)")
    private void fileUpload() {}

    /**
     * {@link FileUpload} 어노테이션이 있으면 서버에 파일업로드 실행
     * 컨트롤러에서 파일을 받을 때 {@link FileReq} 상속 또는 해당 객체로 받아야한다.
     * <pre> 업로드 한 파일 정보 가져오기
     *     List<FileVo> files = fileUtil.getFileInfo();
     *     for (FileVo file : files) {
     *          file.oriFileName();
     *          file.saveFileName();
     *          file.saveFilePath();
     *    }
     * </pre>
     */
//    @Around("fileUpload()")
//    public Object fileUpload(ProceedingJoinPoint joinPoint) throws Throwable{
//        // 파일 파라미터명 확인, FileReq 상속 받아 사용
//        String fileParam = "multipartFiles";
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Object[] args = joinPoint.getArgs();
//        String[] paramNames = signature.getParameterNames();
//        FileUpload fileUpload = signature.getMethod().getAnnotation(FileUpload.class);
//        // 데이터 세팅
//        String pathId = fileUpload.path();
//        String idParam = fileUpload.idParam();
//        boolean idUsage = fileUpload.idUsage();
//        String memberId = "";
//        FileUtil fileUtil = new FileUtil();
//
//        int idIndex = Util.findIndexArrayValue(paramNames, idParam);
//        if (idIndex != -1 && idUsage) {
//            memberId = (String) args[idIndex];
//        }
//
//        FileReq fileReq = (FileReq) args[Util.findIndexArrayValue(paramNames, fileParam)];
//        if (!fileUtil.uploadFiles(fileReq.getMultipartFiles(), memberId, pathId)) {
//            throw new CustomException(ErrorCode.FailedSaveFile);
//        }
//
//        return joinPoint.proceed(args);
//    }
}
