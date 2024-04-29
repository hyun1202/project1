package com.jobty.global.mail.model;

import lombok.Getter;

@Getter
public class UrlParam {
    String key;
    String value;

    public UrlParam(String key, String value){
        this.key = key;
        this.value = value;
    }
}
