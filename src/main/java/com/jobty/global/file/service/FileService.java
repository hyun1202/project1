package com.jobty.global.file.service;

import com.jobty.advice.exception.CustomException;
import com.jobty.advice.exception.ErrorCode;
import com.jobty.conf.property.GlobalProperty;
import com.jobty.global.file.domain.File;
import com.jobty.global.file.dto.FileDto;
import com.jobty.global.file.dto.FileReq;
import com.jobty.global.file.repository.FileRepository;
import com.jobty.util.FileUtil;
import com.jobty.util.Util;
import com.jobty.util.cipher.CipherUtil;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

@Service
public class FileService {
    private final FileRepository fileRepository;
    @Setter
    private String basicPath;
    private final String separator = java.io.File.separator;

    FileService(FileRepository fileRepository,
                GlobalProperty.Jobty.File fileProperty){
        this.fileRepository = fileRepository;
        this.basicPath = fileProperty.getUpload_path();
    }


    /**
     * 기본 저장 경로를 포함한 전체 파일 경로를 가져온다.
     * @param path 기본 저장 경로가 제외 된 파일 저장 경로
     * @return 전체 파일 경로
     */
    public String getFullPath(String path){
        return this.basicPath.replaceAll("/", this.separator) + path.replaceAll("/", Matcher.quoteReplacement(this.separator));
    }

    /**
     * 파일 업로드
     * @param path 업로드할 경로
     * @param reqFile 업로드할 파일 정보
     * @return 업로드 완료한 파일 정보
     */
    public List<FileDto> uploadFiles(String path, FileReq reqFile){
        return uploadFiles(path, path, reqFile);
    }

    /**
     * 파일 업로드 (저장 경로와 요청 경로가 다를 경우 사용)
     * @param path 업로드할 경로
     * @param savePath 저장할 경로
     * @param reqFile 업로드할 파일 정보
     * @return 업로드 완료한 파일 정보
     */
    public List<FileDto> uploadFiles(String path, String savePath, FileReq reqFile) {
        // 파일 경로의 첫번째가 파일 구분자가 아니면 구분자를 붙여준다.
        path = setFirstSeparator(path);
        savePath = setFirstSeparator(savePath);
        // 실제 저장 경로 구하기
        String fullPath = this.basicPath + savePath;
        List<FileDto> uploadFiles = new ArrayList<>();

        for (MultipartFile file : reqFile.getMultipartFiles()) {
            // 파일 원래 이름
            String oriFileName = Normalizer.normalize(Objects.requireNonNull(file.getOriginalFilename()), Normalizer.Form.NFC);
            String extension = FileUtil.getFileExtension(oriFileName);
            // 파일 이름 랜덤 생성
            String saveFileName = Util.random() + "." + extension;
            // 전체 파일 저장 위치
            String saveFilePath = fullPath + this.separator + saveFileName;
            // 파일 저장 성공
            if (FileUtil.save(file, saveFilePath)){
                uploadFiles.add(
                    FileDto.builder()
                            .oriFileName(oriFileName)
                            .saveFileName(saveFileName)
                            .saveFilePath(path + saveFileName)
                            .extension(extension)
                            .build()
                );
            }
            // 파일 저장 실패
            else{
                // 현재 저장된 파일 모두 삭제 처리한다.
                for (FileDto uploadFile: uploadFiles){
                    saveFilePath = uploadFile.getSaveFilePath();
                    // 파일 삭제
                    new java.io.File(saveFilePath + "." + uploadFile.getExtension()).delete();
                }
                throw new CustomException(ErrorCode.FailedSaveFile);
            }
        }
        return uploadFiles;
    }

    public String setFirstSeparator(String path){
        if (path.charAt(0) != this.separator.charAt(0)){
            return this.separator + path;
        }
        return path;
    }

    /**
     * 파일 아이디에 맞는 파일 다운로드
     * @param fileId 파일 아이디
     * @return 파일 정보 리턴
     */
    public FileDto downloadFile(int fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new CustomException(ErrorCode.FailedDownloadFile));
        return FileDto.builder()
                .oriFileName(file.getOriFileName())
                .saveFileName(file.getSaveFileName())
                .saveFilePath(file.getFilePath())
                .extension(FileUtil.getFileExtension(file.getFilePath()))
                .build();
    }

    /**
     * 파일 경로에 맞는 파일 다운로드
     * @param uri 파일 경로
     * @return 파일 정보 리턴
     */
    public FileDto downloadFile(String uri){
        return FileDto.builder()
                .oriFileName("")
                .saveFilePath(getFullPath(uri))
                .extension(FileUtil.getFileExtension(uri))
                .build();
    }

    /**
     * 파일 데이터 데이터 베이스에 저장
     * @param postId 게시글 아이디
     * @param files 저장한 파일 정보
     */
    public void saveFiles(Long postId, List<FileDto> files){
        for (FileDto file : files){
            fileRepository.save(
                    File.builder()
                            .postSeq(postId)
                            .oriFileName(file.getOriFileName())
                            .saveFileName(file.getSaveFileName())
                            .filePath(file.getSaveFilePath())
                            .build()
            );
        }
    }

    /**
     * img src로 된 이미지를 보여준다.
     * @param uri 이미지 경로
     * @return 해당 경로의 Resource 이미지
     */
    public Resource showImage(String uri) {
        // TODO 이미지인지 확인하는 로직 작성
        String path = this.basicPath + uri;
        try {
            return FileUtil.download(path);
        }catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.FailedDownloadFile);
        }
    }

    /**
     * 파일을 삭제한다.
     * @param file 삭제할 파일 정보
     */
    public void deleteFile(FileDto.Delete file) {
        // 폴더의 id와 요청한 id값이 동일한지 확인
        String encId = getEncUid(file.getUid());
        String uriId = file.getSaveFilePath().split("/")[1];
        if (!encId.equals(getEncUid(uriId)))
            throw new CustomException(ErrorCode.FileNotHavePermission);
        // 파일 삭제
        String filePath = file.getSaveFilePath().replace(uriId, encId);
        deleteFile(filePath);
    }

    /**
     * 파일을 삭제한다.
     * @param path 삭제할 파일 경로
     */
    public void deleteFile(String path) {
        // 파일 삭제
        // TODO path에 있는 separator 통합 필요..
        FileUtil.delete(this.basicPath + path);
    }

    /**
     * uid를 암호화한 값을 가져온다.
     * @param uid 회원 uid
     * @return uid 암호화 문자열
     */
    public String getEncUid(String uid){
        return CipherUtil.encrypt(CipherUtil.NORMAL, uid);
    }
}
