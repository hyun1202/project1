package com.hyun.jobty.member.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "token", timeToLive = 60 * 60 * 1000L)
public class Token {
    @Id @Indexed
    private String memberId;
    private String accessToken;
    private String refreshToken;

    public Token(){}

    @Builder
    public Token(String memberId, String accessToken, String refreshToken){
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
