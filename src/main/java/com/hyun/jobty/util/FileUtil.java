package com.hyun.jobty.util;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class FileUtil {

    /**
     * 파일 저장, 폴더가 없으면 하위 폴더까지 전부 생성한다.
     * @param saveFile 저장할 파일
     * @param fullPath 저장할 전체 파일 경로
     * @return 파일 저장 성공 여부
     */
    public static boolean save(MultipartFile saveFile, String fullPath){
        try{
            File folder = new File(fullPath);
            // 폴더가 없으면 하위 폴더까지 모두 생성
            if (!folder.exists()){
                folder.mkdirs();
            }
            // 파일 객체 생성 후 파일 저장
            File dst = new File(fullPath);
            // 실제 파일 저장
            saveFile.transferTo(dst);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 파일 다운로드(img src 또는 실제 파일 다운로드에 사용)
     * @param fullPath 다운로드할 전체 파일 경로
     * @return 해당하는 파일 urlResource
     * @throws MalformedURLException 리소스에 맞지 않는 url 포맷일 때 발생
     */
    public static UrlResource download(String fullPath) throws MalformedURLException {
        return new UrlResource("file:"  + fullPath);
    }

    /**
     * 파일을 삭제한다.
     * @param fullPath 전체 파일 경로
     * @return 파일 삭제 성공 여부
     */
    public static boolean delete(String fullPath){
        if (new File(fullPath).delete()){
            return true;
        }
        throw new CustomException(ErrorCode.FailedDeleteFile);
    }

    /**
     * 해당하는 파일의 확장자를 가져온다.
     * @param fileName 파일 이름
     * @return 확장자 문자열
     */
    public static String getFileExtension(String fileName){
        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension == null)
            return "";
        return extension;
    }
}
