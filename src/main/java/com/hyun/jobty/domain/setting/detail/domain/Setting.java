package com.hyun.jobty.domain.setting.detail.domain;

import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.setting.template.domain.Template;
import com.hyun.jobty.domain.member.domain.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "setting")
public class Setting extends Timestamped {
    @Id
    private String domain;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    private Member member;
    @Column(name = "favicon_img")
    private String faviconImg;
    @Column(name = "blog_name")
    private String blogName;
    @Column(name = "blog_description")
    private String blogDescription;
    @Column(name = "blog_keyword")
    private String blogKeyword;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_seq")
    private Template template;
    @Column(name = "template_use")
    private String templateUse;

    @Builder
    public Setting(String domain, Member member, String faviconImg, String blogName, String blogDescription, String blogKeyword, Template template, String templateUse){
        this.domain = domain;
        this.member = member;
        this.faviconImg = faviconImg;
        this.blogName = blogName;
        this.blogDescription = blogDescription;
        this.blogKeyword = blogKeyword;
        this.template = template;
        this.templateUse = templateUse;
    }

    /**
     * 키워드 목록 리턴
     * @return 키워드 목록
     */
    public List<String> getBlogKeyword(){
        List<String> keyword = List.of(this.blogKeyword.split(","));
        return keyword;
    }

    /**
     * 템플릿 사용 목록 리턴
     * @return 템플릿 사용 목록
     */
    public List<String> getTemplateUse(){
        List<String> template = List.of(this.templateUse.split(","));
        return template;
    }
}
