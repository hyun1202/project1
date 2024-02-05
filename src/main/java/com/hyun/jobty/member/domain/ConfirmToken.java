package com.hyun.jobty.member.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "confirm", timeToLive = 30 * 60 )
public class ConfirmToken {
    @Id @Indexed
    private String token;
    private String userId;
    private int seq;

    @Builder
    public ConfirmToken(String token, String userId, int seq){
        this.token = token;
        this.userId = userId;
        this.seq = seq;
    }
}
