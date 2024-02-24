package com.hyun.jobty.domain.blog.dto;

import lombok.Getter;

@Getter
public class AddPostReq {
    private String thumbnail;
    private String title;
    private String content;
    public AddPostReq(){}
    public AddPostReq(String thumbnail, String title, String content){
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
    }
}
