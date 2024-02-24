package com.hyun.jobty.domain.setting.menu.domain;

import com.hyun.jobty.domain.member.domain.Timestamped;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu")
@Entity
@Getter
public class Menu extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_seq")
    private int seq;
    @OneToOne
    @JoinColumn(name = "domain")
    private Setting setting;
    @OneToOne
    @JoinColumn(name = "blog_main_category_seq")
    private BlogMainCategory mainCategory;
    @Column(name = "blog_sub_category_name")
    private String subCategoryName;
    @Column(name = "menu_name")
    private String name;
    @Column(name = "menu_upper_seq")
    private Long upperSeq;
    @Column(name = "menu_depth")
    private int depth;
    @Column(name = "menu_sort_no")
    private int sortNo;
    @Column(name = "menu_group_no")
    private int groupNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_upper_seq", insertable=false, updatable=false)
    private Menu main;
    @OneToMany(mappedBy = "main")
    @OrderBy("sortNo asc")
    private List<Menu> sub = new ArrayList<>();

    @Builder
    public Menu(int seq, Setting setting, BlogMainCategory mainCategory, String subCategoryName, String name, Long upperSeq, int depth, int sortNo, int groupNo){
        this.seq = seq;
        this.setting = setting;
        this.mainCategory = mainCategory;
        this.subCategoryName = subCategoryName;
        this.name = name;
        this.upperSeq = upperSeq;
        this.depth = depth;
        this.sortNo = sortNo;
        this.groupNo = groupNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    /**
     * 대메뉴 생성
     * @param mainCategory {@link BlogMainCategory}
     */
    public void createMainMenu(BlogMainCategory mainCategory) {
        this.mainCategory = mainCategory;
        this.depth = 0;
    }

    /**
     * 소메뉴 생성
     * @param subCategoryName 소메뉴 카테고리명
     * @param upperSeq 상위 메뉴 분류 번호
     */
    public void createSubMenu(String subCategoryName, Long upperSeq) {
        this.subCategoryName = subCategoryName;
        this.upperSeq = upperSeq;
        this.depth = 1;
    }

    public void updateMainMenu(BlogMainCategory category, String menu_name){
        this.mainCategory = category;
        this.name = menu_name;
    }


    public void updateSubMenu(String subCategoryName, String menu_name){
        this.subCategoryName = subCategoryName;
        this.name = menu_name;
    }

    /**
     * 대메뉴 카테고리명과 소메뉴의 카테고리명이 다르므로 분기 처리
     * @return 카테고리명 리턴
     */
    public String getMenuCategoryName(){
        if (this.subCategoryName == null)
            return this.mainCategory.getMainCategoryName();
        return this.subCategoryName;
    }
}
