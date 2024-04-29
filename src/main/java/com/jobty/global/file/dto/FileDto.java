package com.jobty.global.file.dto;

import com.jobty.global.accountValidator.dto.ValidatorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class FileDto {
    private String extension;
    private String saveFileName;
    private String oriFileName;
    private String saveFilePath;

    public FileDto(FileDto fileDto){
        this.extension = fileDto.getExtension();
        this.saveFileName = fileDto.getSaveFileName();
        this.oriFileName = fileDto.getOriFileName();
        this.saveFilePath = fileDto.getSaveFilePath();
    }
    @Builder
    public FileDto(String extension, String saveFileName, String oriFileName, String saveFilePath){
        this.extension = extension;
        this.saveFileName = saveFileName;
        this.oriFileName = oriFileName;
        this.saveFilePath = saveFilePath.replaceAll("\\\\", "/");
    }

    @Getter
    @NoArgsConstructor
    public static class Delete extends ValidatorDto {
        private String saveFilePath;
    }

    @AllArgsConstructor
    @Builder @Getter
    public static class Upload {
        String uid;
        String encUid;
        String path;
        String savePath;
        List<MultipartFile> multipartFiles;
    }
}
