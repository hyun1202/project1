package com.hyun.jobty.global.file.service;

import com.hyun.jobty.global.file.FileVo;
import com.hyun.jobty.global.file.dto.FileDTO;
import com.hyun.jobty.global.file.dto.FileReq;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileService {
    List<FileVo> uploadFiles(String path, FileReq req);
    public FileVo getDownloadFilePath(int file_id);
    public void saveFiles(Long post_id, List<FileVo> files);

    Resource showImage(String full_path);
    public void deleteFile(FileDTO fileDTO);
}
