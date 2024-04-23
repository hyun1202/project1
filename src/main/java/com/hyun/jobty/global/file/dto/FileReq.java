package com.hyun.jobty.global.file.dto;

import com.hyun.jobty.global.accountValidator.dto.ValidatorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class FileReq extends ValidatorDto {
    @Schema(description = "type이 setting이 아닐 때 사용")
    private Long post_id;
    @Schema(description = "업로드할 파일")
    List<MultipartFile> multipartFiles;

    @Builder
    public FileReq(String uid, Long post_id, List<MultipartFile> multipartFiles){
        this.setUid(uid);
        this.post_id = post_id;
        this.multipartFiles = multipartFiles;
    }

    public String strPostId() {
        if (this.post_id == null)
            return "";
        return String.valueOf(this.post_id);
    }
}