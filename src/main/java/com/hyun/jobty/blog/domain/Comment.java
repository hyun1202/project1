package com.hyun.jobty.blog.domain;

import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.member.domain.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "post_comment")
public class Comment extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_comment_seq")
    private int seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_seq")
    private Post post;
    @Column(name = "comment")
    private String content;
    @Column(name = "private_yn")
    private String privateYn;
    @JoinColumn(name = "member_seq")
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;
    @Column(name = "upper_post_no")
    private int upperNo;
    @Column(name = "group_ord")
    private int ord;
    @Column(name = "group_depth")
    private int depth;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_no", insertable = false, updatable = false)
    private Comment comment;
    @OneToMany(mappedBy = "comment")
    private List<Comment> reply = new ArrayList<>();

    @Builder
    public Comment(int seq, Post post, String content, boolean isPrivate, Member member, int upperNo, int ord, int depth){
        this.seq = seq;
        this.post = post;
        this.content = content;
        this.privateYn = isPrivate ? "Y" : "N";
        this.member = member;
        this.upperNo = upperNo;
        this.ord = ord;
        this.depth = depth;
    }

    public boolean getPrivateYn() {
        if (this.privateYn.equals("Y"))
            return true;
        return false;
    }

    /**
     * 댓글 비공개 설정
     */
    public void setPrivateComment(){
        this.privateYn = "Y";
    }
}
