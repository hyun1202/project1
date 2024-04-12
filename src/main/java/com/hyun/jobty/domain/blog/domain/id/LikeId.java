package com.hyun.jobty.domain.blog.domain.id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeId implements Serializable {
    private Long postSeq;
    private int memberSeq;
    @Builder
    public LikeId(Long postSeq, int memberSeq){
        this.postSeq = postSeq;
        this.memberSeq = memberSeq;
    }
}
