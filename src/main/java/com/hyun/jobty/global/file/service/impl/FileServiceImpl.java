package com.hyun.jobty.global.file.service.impl;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.global.file.FileUtil;
import com.hyun.jobty.global.file.FileVo;
import com.hyun.jobty.global.file.domain.File;
import com.hyun.jobty.global.file.dto.FileDTO;
import com.hyun.jobty.global.file.dto.FileReq;
import com.hyun.jobty.global.file.repository.FileRepository;
import com.hyun.jobty.global.file.service.FileService;
import com.hyun.jobty.util.Util;
import com.hyun.jobty.util.cipher.CipherUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    /**
     * 파일 업로드
     * @param path 업로드할 경로
     * @param req 업로드할 파일 정보
     * @return 업로드 완료한 파일 정보
     */
    @Override
    public List<FileVo> uploadFiles(String path, FileReq req) {
        FileUtil fileUtil = new FileUtil();
        if (fileUtil.uploadFiles(req.getMultipartFiles(), path)){
            return fileUtil.getMultiFileInfo();
        }
        throw new CustomException(ErrorCode.FailedSaveFile);
    }

    @Override
    public FileVo getDownloadFilePath(int file_id) {
        File file = fileRepository.findById(file_id).orElseThrow(() -> new CustomException(ErrorCode.FailedDownloadFile));
        return new FileVo(file.getOriFileName(), file.getSaveFileName(), file.getFilePath(), Util.getFileExtension(file.getFilePath()));
    }

    /**
     * 파일 데이터 데이터 베이스에 저장
     * @param post_id 게시글 아이디
     * @param files 저장한 파일 정보
     */
    public void saveFiles(Long post_id, List<FileVo> files){
        for (FileVo file : files){
            fileRepository.save(
                    File.builder()
                            .postSeq(post_id)
                            .oriFileName(file.oriFileName())
                            .saveFileName(file.saveFileName())
                            .filePath(file.saveFilePath())
                            .build()
            );
        }
    }

    @Override
    public Resource showImage(String full_path) {
        // 이미지인지 아닌지 확인하는 로직 작성

        return new FileUtil().downloadFile(full_path);
    }

    @Override
    public void deleteFile(FileDTO fileDTO) {
        // 폴더의 id와 요청한 id값이 동일한지 확인
        String enc_id = CipherUtil.encrypt(CipherUtil.NORMAL, fileDTO.getId());
        if (!enc_id.equals(fileDTO.getPath().split("/")[1]))
            throw new CustomException(ErrorCode.FileNotHavePermission);
        // 파일 삭제
        new FileUtil().deleteFile(fileDTO.getPath());
    }
}
