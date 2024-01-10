package com.hyun.jobty.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public abstract class FileRequest {
    List<MultipartFile> multipartFiles;

    public FileRequest(List<MultipartFile> multipartFiles){
        this.multipartFiles = multipartFiles;
    }

    public List<MultipartFile> getMultipartFiles() {
        return multipartFiles;
    }
}