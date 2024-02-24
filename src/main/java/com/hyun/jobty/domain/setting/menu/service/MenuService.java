package com.hyun.jobty.domain.setting.menu.service;

import com.hyun.jobty.domain.setting.menu.dto.MenuDto;
import com.hyun.jobty.domain.setting.menu.domain.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> findMenuByDomain(String id);
    Menu findMenuByMenuId(int seq);
    int deleteSingleMenu(String id, int seq);
    Menu updateSingleMenu(String id, MenuDto.UpdateReq req);
    Menu saveSingleMenu(String id, MenuDto.AddReq req);
    Menu updateSortMainMenu(String id,MenuDto.UpdateReq req);
    Menu updateSortSubMenu(String id,MenuDto.UpdateReq req);
}
