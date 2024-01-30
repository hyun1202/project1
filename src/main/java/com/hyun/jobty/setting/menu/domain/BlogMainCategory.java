package com.hyun.jobty.setting.menu.domain;

import com.hyun.jobty.member.domain.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "blog_main_category")
@Entity
@Getter
public class BlogMainCategory extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_main_category_seq")
    private int seq;
    @Column(name = "blog_main_category_name")
    private String mainCategoryName;

    @Builder
    public BlogMainCategory(int seq, String mainCategoryName) {
        this.seq = seq;
        this.mainCategoryName = mainCategoryName;
    }
}
