package com.hyun.jobty.domain.setting.menu.service;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.domain.setting.detail.service.SettingService;
import com.hyun.jobty.domain.setting.menu.domain.BlogMainCategory;
import com.hyun.jobty.domain.setting.menu.domain.Menu;
import com.hyun.jobty.domain.setting.menu.dto.MenuDto;
import com.hyun.jobty.domain.setting.menu.repository.BlogMainCategoryRepository;
import com.hyun.jobty.domain.setting.menu.repository.MenuCustomRepository;
import com.hyun.jobty.domain.setting.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuService {
    private final SettingService settingService;
    private final MenuRepository menuRepository;
    private final MenuCustomRepository menuCustomRepository;
    private final BlogMainCategoryRepository blogMainCategoryRepository;

    public List<Menu> findMenuByDomain(String domain) {
        return menuRepository.findAllBySetting_DomainAndUpperSeqIsNullOrderBySeq(domain).orElseThrow(() -> new CustomException(ErrorCode.NoMenuData));
    }

    public Menu findMenuByMenuId(int seq) {
        return menuRepository.findById(seq).orElseThrow(() -> new CustomException(ErrorCode.NotFoundMenu));
    }

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

    @Transactional
    public Menu updateSingleMenu(String domain, MenuDto.UpdateReq req) {
        // TODO 도메인, menu_seq로 메뉴 검색하는 repository 함수 필요
        Menu menu = menuRepository.findById(req.getMenu_seq()).orElseThrow(() -> new CustomException(ErrorCode.NotFoundMenu));
        if (menu.getDepth() == 0){
            // 대메뉴 수정일 때 카테고리 번호 변경
            BlogMainCategory blogMainCategory = getBlogMainCategory(req.getMain_category_seq().intValue());
            menu.updateMainMenu(blogMainCategory, req.getMenu_name());
        }else{
            // 소메뉴 수정일 때 카테고리명 변경
            menu.updateSubMenu(req.getMenu_name(), req.getCategory_name());
        }
        return menu;
    }

    @Transactional
    public Menu updateListMenu(String id, MenuDto.UpdateReq req) {
        // TODO: 업데이트의 경우 전체 데이터를 가져오기 때문에 전체 main 메뉴와 sub 메뉴를 받아 처리하도록 변경
        return null;
    }

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

    @Transactional
    public List<Menu> updateSortMenu(String domain, MenuDto.ListReq req){
        // TODO 메뉴 정렬 변경 시 체크할 로직 작성
        // 메뉴 번호를 오름차순으로 정렬
        List<MenuDto.ListReq.Menu> sortedReqMenus = req.getMenus()
                .stream()
                .sorted(Comparator.comparing(MenuDto.ListReq.Menu::getMenu_seq))
                .toList();

        for (MenuDto.ListReq.Menu menu : sortedReqMenus){
            List<MenuDto.Sub> sortedSubs = menu.getSubs()
                    .stream()
                    .sorted(Comparator.comparing(MenuDto.Sub::getMenu_seq))
                    .toList();
            menu.setSubs(sortedSubs);
        }

        // 데이터베이스에서 도메인에 해당하는 메뉴를 오름차순으로 가져온다.
        List<Menu> menus = menuCustomRepository.findAllMenuOrderMenuSeqByDomain(domain);

        // 정렬되어있으므로 차례대로 돌면서 순서 변경
        for (int i=0; i<menus.size(); i++){
            Menu menu = menus.get(i);
            List<Menu> subs = menus.get(i).getSub();
            MenuDto.ListReq.Menu sortedReqMenu = sortedReqMenus.get(i);
            // 정렬 변경할 때 메뉴가 추가되는 경우가 있지..
            // 엥ㅋㅋ 여기선 upper_seq 수정이 안되네?
            // 수정 시에는 upper_seq가 변경이 되는걸로,,,!!!
            // 정렬 변경 시 추가된 메뉴들이 있다면 예외 발생
//            if (menus.size() != sortedReqMenus.size()
//                    || subs.size() != sortedReqMenu.getSubs().size())
//                throw new CustomException(ErrorCode.FAIL);
            menu.setMainSortNo(sortedReqMenus.get(i).getGroup_no());
            for (int j=0; j<subs.size(); j++){
                Menu sub = menu.getSub().get(j);
                sub.setSubSortNo(sortedReqMenu.getSubs().get(j).getSort_no());
            }
        }
        return menus;
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
