package com.hyun.jobty.domain.setting.detail.dto;

import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.global.accountValidator.dto.ValidatorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SettingDto {
    private String domain;
    private String favicon_img;
    private String blog_name;
    private String blog_description;
    private List<String> blog_keyword;

    @Builder
    SettingDto(Setting setting){
        this.domain = setting.getDomain();
        this.favicon_img = setting.getFaviconImg();
        this.blog_name = setting.getBlogName();
        this.blog_description = setting.getBlogDescription();
        this.blog_keyword = setting.getBlogKeyword();
    }
    @Getter
    public static class AddSettingReq{
        @Schema(description = "회원 아이디")
        private String id;
        @Schema(description = "업로드 완료 후 리턴된 썸네일 이미지 경로")
        private String favicon_img;
        @Schema(description = "블로그 이름")
        private String blog_name;
        @Schema(description = "블로그 설명")
        private String blog_description;
        @Schema(description = "키워드, 여러개일 경우 ,로 구분")
        private String blog_keyword;
    }

    @Getter
    public static class AddSettingRes{
        private String favicon_img;
        private String blog_name;
        private String blog_description;
        private List<String> blog_keyword;

        @Builder
        AddSettingRes(Setting setting){
            this.favicon_img = setting.getFaviconImg();
            this.blog_name = setting.getBlogName();
            this.blog_description = setting.getBlogDescription();
            this.blog_keyword = setting.getBlogKeyword();
        }
    }

    @Getter
    public static class DomainReq extends ValidatorDto {
        @NotNull
        private String domain;

        @Builder
        public DomainReq(String domain){
            this.domain = domain;
        }
    }

    @Getter
    public static class DomainRes{
        private String domain;

        @Builder
        public DomainRes(Setting setting){
            this.domain = setting.getDomain();
        }
    }
}
