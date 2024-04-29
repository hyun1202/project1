package com.jobty.domain.blog.domain.id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeId implements Serializable {
    private Long postSeq;
    private String memberUid;
    @Builder
    public LikeId(Long postSeq, String memberUid){
        this.postSeq = postSeq;
        this.memberUid = memberUid;
    }
}
