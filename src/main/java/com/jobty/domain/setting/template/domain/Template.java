package com.jobty.domain.setting.template.domain;

import com.jobty.domain.member.domain.Member;
import com.jobty.domain.member.domain.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template")
@Entity
@Getter
public class Template extends Timestamped {
    @Id
    @Column(name = "template_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_uid")
    private Member member;
    private String thumbnail;
    @Column(name = "template_name")
    private String name;
    @Column(name = "template_keyword")
    private String keyword;

    @Builder
    public Template(int seq, Member member, String thumbnail, String name, String keyword){
        this.seq = seq;
        this.member = member;
        this.thumbnail = thumbnail;
        this.name = name;
        this.keyword = keyword;
    }
}
