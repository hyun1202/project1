package com.hyun.jobty.domain.setting.menu.service;

import com.hyun.jobty.domain.setting.menu.dto.MenuDto;
import com.hyun.jobty.domain.setting.menu.domain.Menu;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuService {
    List<Menu> findMenuByDomain(String domain);
    Menu findMenuByMenuId(int seq);
    int deleteSingleMenu(String domain, int seq);
    Menu updateSingleMenu(String id, MenuDto.UpdateReq req);
    Menu saveSingleMenu(String domain, MenuDto.Req req);
    @Transactional
    Menu updateSortMenu(String domain, MenuDto.UpdateReq req);
}
