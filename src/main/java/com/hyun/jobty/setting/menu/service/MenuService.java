package com.hyun.jobty.setting.menu.service;

import com.hyun.jobty.setting.menu.domain.Menu;
import com.hyun.jobty.setting.menu.dto.MenuDto;

import java.util.List;

public interface MenuService {
    List<Menu> findByMenu(String id);
    Menu findBySingleMenu(int seq);
    int deleteSingleMenu(String id, int seq);
    Menu updateSingleMenu(String id, MenuDto.UpdateReq req);
    Menu saveSingleMenu(String id, MenuDto.AddReq req);
    Menu updateSortMainMenu(String id,MenuDto.UpdateReq req);
    Menu updateSortSubMenu(String id,MenuDto.UpdateReq req);
}
