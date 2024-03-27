package com.hyun.jobty.global.file;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.conf.property.ApplicationContextProvider;
import com.hyun.jobty.conf.property.GlobalProperty;
import com.hyun.jobty.util.Util;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class FileUtil {
    private String basicPath;
    private List<FileVo> fileInfo = new ArrayList<>();

    public FileUtil(){
        // 파일 저장 위치 설정 파일에서 가져옴
        this.basicPath = ApplicationContextProvider.getBean("globalProperty.Jobty.File", GlobalProperty.Jobty.File.class).getUpload_path();
    }

    // 저장 위치 변경하고 싶은 경우 사용
    public void setBasicPath(String basicPath) {
        this.basicPath = basicPath;
    }

    /**
     * 저장할 파일이 여러개일 때 사용
     * @return 저장한 파일들 정보
     */
    public List<FileVo> getMultiFileInfo() {
        return this.fileInfo;
    }

    /**
     * 파일이 하나일 때 사용
     * @return 저장한 파일 정보
     */
    public FileVo getSingleFileInfo() {
        return this.fileInfo.get(0);
    }

    /**
     * 저장할 파일이 하나일 때 사용
     * @param file 실제 저장할 파일
     * @param path 저장할 경로
     * @return 파일 생성 성공 여부
     */
    public Boolean uploadFile(MultipartFile file, String path){
        return saveFile(List.of(file), path);
    }

    /**
     * 저장할 파일이 여러개일 때 사용
     * @param files 실제 저장할 파일들
     * @param path 저장할 경로
     * @return 파일 생성 성공 여부
     */
    public Boolean uploadFiles(List<MultipartFile> files, String path){
        return saveFile(files, path);
    }

    /**
     * 파일 저장
     * 파일 저장에 실패시 실패한 파일들은 전부 삭제 처리
     * @param files 실제 저장할 파일들
     * @param path 저장할 경로
     * @return 파일 저장 성공 여부
     */
    private Boolean saveFile(List<MultipartFile> files, String path){
        String folder_path = basicPath + File.separator + path;
        try{
            File folder = new File(folder_path);
            // 폴더가 없으면 하위 폴더까지 모두 생성
            if (!folder.exists()){
                folder.mkdirs();
            }
            for (MultipartFile file : files){
                String oriFileName = Normalizer.normalize(Objects.requireNonNull(file.getOriginalFilename()), Normalizer.Form.NFC);
                // 파일 이름 랜덤 생성
                String saveFileName = Util.random() + "." + Util.getFileExtension(oriFileName);
                // 전체 파일 저장 위치
                String saveFilePath = path + File.separator + saveFileName;
                // 파일 객체 생성 후 파일 저장
                File dst = new File(folder_path + File.separator + saveFileName);
                // 실제 파일 저장
                file.transferTo(dst);
                // 파일을 저장하고 fileInfo에 저장
                fileInfo.add(new FileVo(oriFileName, saveFileName, saveFilePath, Util.getFileExtension(oriFileName)));
            }
            return true;
        }catch (Exception e){
            // 실패 시 모두 삭제 처리한다.
            for (FileVo file : fileInfo){
                String saveFilePath = file.saveFilePath();
                // 파일 삭제
                new File(saveFilePath + "." + Util.getFileExtension(saveFilePath)).delete();
            }
        }
        return false;
    }

    public UrlResource downloadFile(String save_path) {
        try {
            return new UrlResource("file:"  + getFullPath(save_path));
        }catch (Exception e){
            throw new CustomException(ErrorCode.FailedDownloadFile);
        }

    }

    public String getFullPath(String path){
        return basicPath.replaceAll("/", File.separator) + path.replaceAll("/", Matcher.quoteReplacement(File.separator));
    }

    public boolean deleteFile(String path){
        try {
            new File(getFullPath(path)).delete();
            return true;
        }catch (Exception e){
            throw new CustomException(ErrorCode.FailedDeleteFile);
        }
    }
}
