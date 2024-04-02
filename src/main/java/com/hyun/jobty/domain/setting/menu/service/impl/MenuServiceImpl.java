package com.hyun.jobty.domain.setting.menu.service.impl;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.domain.setting.detail.service.SettingService;
import com.hyun.jobty.domain.setting.menu.domain.BlogMainCategory;
import com.hyun.jobty.domain.setting.menu.domain.Menu;
import com.hyun.jobty.domain.setting.menu.dto.MenuDto;
import com.hyun.jobty.domain.setting.menu.repository.BlogMainCategoryRepository;
import com.hyun.jobty.domain.setting.menu.repository.MenuRepository;
import com.hyun.jobty.domain.setting.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuServiceImpl implements MenuService {
    private final SettingService settingService;
    private final MenuRepository menuRepository;
    private final BlogMainCategoryRepository blogMainCategoryRepository;

    @Override
    public List<Menu> findMenuByDomain(String domain) {
        return menuRepository.findAllBySetting_DomainAndUpperSeqIsNullOrderBySeq(domain).orElseThrow(() -> new CustomException(ErrorCode.NoMenuData));
    }

    @Override
    public Menu findMenuByMenuId(int seq) {
        return menuRepository.findById(seq).orElseThrow(() -> new CustomException(ErrorCode.NotFoundMenu));
    }

    @Override
    public int deleteSingleMenu(String domain, int seq) {
        // 게시글 또는 하위 메뉴가 있으면 삭제 불가
        // 하위 메뉴 검사
        if (menuRepository.existsBySetting_DomainAndUpperSeq(domain, seq)){
            throw new CustomException(ErrorCode.ExistsUpperMenu);
        }
        // 게시글 검사

        Menu menu = menuRepository.findById(seq).orElseThrow(() -> new CustomException(ErrorCode.FailedDeleteMenu));
        menuRepository.delete(menu);
        return menu.getSeq();
    }

    @Override
    @Transactional
    public Menu updateSingleMenu(String id, MenuDto.UpdateReq req) {
        return null;
//        Menu menu = menuRepository.findById(req.getMenu_seq()).orElseThrow(() -> new CustomException(ErrorCode.NotFoundMenu));
//        if (menu.getDepth() == 0){
//            // 대메뉴 수정일 때 카테고리 번호 변경
//            BlogMainCategory blogMainCategory = getBlogMainCategory(req.getMain_category_seq().intValue());
//            menu.updateMainMenu(blogMainCategory, req.getMenu_name());
//        }else{
//            // 소메뉴 수정일 때 카테고리명 변경
//            menu.updateSubMenu(req.getMenu_name(), req.getCategory_name());
//        }
//        return menu;
    }

    @Override
    public Menu saveSingleMenu(String domain, MenuDto.Req req) {
        Setting setting = getSetting_SettingByDomain(domain);
        Menu menu = Menu.builder()
                .setting(setting)
                .name(req.getMenu_name())
                .sortNo(req.getSort_no())
                .groupNo(req.getGroup_no())
                .build();
        // 대메뉴
        if (req.getDepth() == 0){
            menu.createMainMenu(getBlogMainCategory(req.getMain_category_seq().intValue()));
        }

        // 소메뉴
        if (req.getDepth() > 0){
            // 상위 메뉴 분류번호가 존재하는지 확인
            if (!menuRepository.existsBySetting_DomainAndSeqAndUpperSeqIsNull(getSetting_DomainById(domain), req.getUpper_menu_seq().intValue())){
                throw new CustomException(ErrorCode.NotfoundMainCategory);
            }
            menu.createSubMenu(req.getCategory_name(), req.getUpper_menu_seq());
        }
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public Menu updateSortMenu(String domain, MenuDto.UpdateReq req){
        // TODO 메뉴 정렬 변경 시 체크할 로직 작성
        // 메뉴 번호를 오름차순으로 정렬
        List<MenuDto.UpdateReq.Menu> updateMenus = req.getMenus()
                .stream()
                .sorted(Comparator.comparing(MenuDto.UpdateReq.Menu::getMenu_seq))
                .toList();
        // 데이터베이스에서 도메인에 해당하는 메뉴를 오름차순으로 가져온다.
        List<Menu> menus = findMenuByDomain(domain);
        System.out.println("asdf");
        // 정렬 변경 시 추가된 메뉴들이 있다면?? 예외처리??

        return null;
    }


    private BlogMainCategory getBlogMainCategory(int seq){
        return blogMainCategoryRepository.findBySeq(seq).orElseThrow(() -> new CustomException(ErrorCode.IncorrectCategory));
    }

    private Setting getSetting_SettingByDomain(String domain){
        return settingService.findByDomain(domain);
    }

    private String getSetting_DomainById(String id){
        return settingService.findByDomain(id).getDomain();
    }
}
