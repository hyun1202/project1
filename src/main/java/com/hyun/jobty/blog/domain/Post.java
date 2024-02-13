package com.hyun.jobty.blog.domain;

import com.hyun.jobty.member.domain.Timestamped;
import com.hyun.jobty.setting.detail.domain.Setting;
import com.hyun.jobty.setting.menu.domain.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "post")
public class Post extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_seq")
    private int seq;
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
    private List<Comment> comments;

    @Builder
    public Post(int seq, Menu menu, Setting setting, String thumbnail, String title, String content, List<Comment> comments){
        this.seq = seq;
        this.menu = menu;
        this.setting = setting;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.comments = comments;
    }
}
