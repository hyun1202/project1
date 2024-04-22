package com.hyun.jobty.domain.member.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "token")
public class Token {
    @Id
    private String id;
    private String uid;
    private String email;
    private String accessToken;
    //login일 때만 사용
    private String refreshToken;
    @TimeToLive
    private Long exp;

    @Builder
    public Token(String id, String uid,String email, String accessToken, String refreshToken, Long exp){
        this.id = id;
        this.uid = uid;
        this.email = email;
        this.accessToken= accessToken;
        this.refreshToken = refreshToken;
        this.exp = exp;
    }

    public void updateToken(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


}
