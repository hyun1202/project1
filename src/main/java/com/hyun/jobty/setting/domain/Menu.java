package com.hyun.jobty.setting.domain;

import com.hyun.jobty.member.domain.Timestamped;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "main_menu_seq")
    private MainMenu mainMenu;
    @Column(name = "menu_name")
    private String name;
    @Column(name = "menu_upper_seq")
    private int upperSeq;
    @Column(name = "menu_depth")
    private int depth;
    @Column(name = "menu_sort_no")
    private int sortNo;

    @Builder
    public Menu(int seq, Setting setting, MainMenu mainMenu, String name, int upperSeq, int depth, int sortNo){
        this.seq = seq;
        this.setting = setting;
        this.mainMenu = mainMenu;
        this.name = name;
        this.upperSeq = upperSeq;
        this.depth = depth;
        this.sortNo = sortNo;
    }
}
