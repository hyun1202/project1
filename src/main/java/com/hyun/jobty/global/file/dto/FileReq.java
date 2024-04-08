package com.hyun.jobty.global.file.dto;

import com.hyun.jobty.util.cipher.CipherUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class FileReq {
    @Schema(description = "유저 아이디(이메일)")
    private String id;
    @Schema(description = "type이 setting이 아닐 때 사용")
    private Long post_id;
    @Schema(description = "업로드할 파일")
    List<MultipartFile> multipartFiles;

    @Builder
    public FileReq(String id, Long post_id, List<MultipartFile> multipartFiles){
        this.id = id;
        this.post_id = post_id;
        this.multipartFiles = multipartFiles;
    }

    public String EncId() {
        return CipherUtil.encrypt(CipherUtil.NORMAL, this.id);
    }

    public String strPostId() {
        if (this.post_id == null)
            return "";
        return String.valueOf(this.post_id);
    }
}