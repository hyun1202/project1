package com.hyun.jobty.domain.blog.domain;

import com.hyun.jobty.domain.blog.dto.PostDto;
import com.hyun.jobty.domain.member.domain.Timestamped;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.domain.setting.menu.domain.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "post")
public class Post extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_seq")
    private Long seq;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_seq")
    private Menu menu;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain")
    private Setting setting;
    private String thumbnail;
    private String title;
    private String content;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @SQLRestriction("group_depth = 0")
    @OrderBy("seq asc")
    private List<Comment> comments;
    @Formula("(select count(1) from post_like a where a.post_seq = post_seq)")
    private Long likeCnt;
    @Formula("(select count(1) from post_view a where a.post_seq = post_seq)")
    private Long viewCnt;

    @Builder
    public Post(Long seq, Menu menu, Setting setting, String thumbnail, String title, String content, List<Comment> comments){
        this.seq = seq;
        this.menu = menu;
        this.setting = setting;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.comments = comments;
    }

    public void updatePost(Long menu_seq, PostDto.AddReq updatePost){
        this.menu.
    }
}
