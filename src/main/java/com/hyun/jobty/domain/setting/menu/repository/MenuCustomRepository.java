package com.hyun.jobty.domain.setting.menu.repository;

import com.hyun.jobty.domain.setting.menu.domain.Menu;

import java.util.List;

public interface MenuCustomRepository {
    List<Menu> findByDomainMenu(String domain);
    boolean existsByUpperMenu(String domain, int seq);
    boolean existsBySubMenu(String domain, int seq);
    int countTopByGroupNo(String domain, int groupNo);
}
