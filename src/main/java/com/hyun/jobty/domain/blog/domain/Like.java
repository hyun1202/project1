package com.hyun.jobty.domain.blog.domain;

import com.hyun.jobty.domain.blog.domain.id.LikeId;
import com.hyun.jobty.domain.member.domain.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

//@AllArgsConstructor @Builder
@Entity @Table(name = "post_like")
@Getter
@IdClass(LikeId.class)
public class Like extends Timestamped {
    @Id @Column(name = "post_seq")
    private Long postSeq;
    @Id @Column(name = "member_uid")
    private String memberUid;

    @Builder
    public Like(Long postSeq, String memberUid) {
        this.postSeq = postSeq;
        this.memberUid = memberUid;
    }

    public Like() {

    }

    /**
     * 복합키 instance 리턴
     */
    public LikeId getLikeIdInstance(){
        return LikeId.builder()
                .postSeq(this.postSeq)
                .memberUid(this.memberUid)
                .build();
    }
}
