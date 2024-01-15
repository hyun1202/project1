package com.hyun.jobty.setting.domain;

import com.hyun.jobty.member.domain.Timestamped;
import jakarta.persistence.*;

@Table(name = "main_menu")
@Entity
public class MainMenu extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_menu_seq")
    private int seq;
    @Column(name = "main_menu_name")
    private String name;
}
