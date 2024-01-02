package com.hyun.jobty.setting.dto;

import com.hyun.jobty.setting.domain.Setting;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class SettingDto {
    @Getter
    public static class AddSettingReq{
        private String favicon_img;
        private String blog_name;
        private String blog_description;
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
    public static class DomainReq{
        private String id;
        @NotNull
        private String domain;

        @Builder
        public DomainReq(String id, String domain){
            this.id = id;
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

    @Getter
    public static class FaviconReq{
        private List<MultipartFile> multipartFiles;

        public FaviconReq(List<MultipartFile> multipartFiles){
            this.multipartFiles = multipartFiles;
        }
    }
}
