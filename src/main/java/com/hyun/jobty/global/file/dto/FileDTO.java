package com.hyun.jobty.global.file.dto;

import com.hyun.jobty.global.file.FileVo;
import lombok.Getter;

@Getter
public class FileDTO {
    private String id;
    private String path;
    FileDTO(){}
    public FileDTO(FileVo fileVo){
        this.path = fileVo.saveFilePath().replaceAll("\\\\", "/");
    }
}
