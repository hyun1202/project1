package com.hyun.jobty.domain.setting.menu.controller;

import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.response.ListResult;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.domain.setting.menu.domain.Menu;
import com.hyun.jobty.domain.setting.menu.dto.MenuDto;
import com.hyun.jobty.domain.setting.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "메뉴 컨트롤러", description = "메뉴 API *** group_no: 대메뉴 정렬 값, sort_no: 소메뉴 정렬 값 ***")
@RequiredArgsConstructor
@RequestMapping("/menu/")
@RestController
public class MenuController {
    private final MenuService menuService;
    private final ResponseService responseService;

    @Operation(summary = "메뉴 조회", description = "id에 해당하는 메뉴 조회")
    @GetMapping(value ="/{id}")
    @AccountValidator
    public ResponseEntity<ListResult<MenuDto.Read>> getMenu(@PathVariable("id") String id){
        List<Menu> menu = menuService.findMenuByDomain(id);
        List<MenuDto.Read> res = menu.stream().map(MenuDto.Read::new).toList();
        return responseService.getListResult(res);
    }

    @Operation(summary = "메뉴 단건 저장", description = "id에 해당하는 메뉴 저장")
    @PostMapping(value ="/{id}")
    @AccountValidator
    public ResponseEntity<SingleResult<MenuDto.Res>> saveMenu(@PathVariable("id") String id, @RequestBody MenuDto.AddReq req){
        Menu menu = menuService.saveSingleMenu(id, req);
        MenuDto.Res res = new MenuDto.Res(menu);
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "메뉴 단건 삭제", description = "id에 해당하는 메뉴 삭제")
    @DeleteMapping(value ="/{id}/{menu_id}")
    @AccountValidator
    public ResponseEntity<SingleResult<Integer>> removeMenu(@PathVariable("id") String id, @PathVariable("menu_id") int menu_id){
        return responseService.getSingleResult(menuService.deleteSingleMenu(id, menu_id));
    }

    @Operation(summary = "메뉴 단건 수정", description = "id에 해당하는 메뉴 업데이트(정렬 제외)")
    @PutMapping(value ="/{id}/")
    @AccountValidator
    public ResponseEntity<SingleResult<MenuDto.Res>> updateMenu(@PathVariable("id") String id, @RequestBody MenuDto.UpdateReq req){
        Menu menu = menuService.updateSingleMenu(id, req);
        MenuDto.Res res = new MenuDto.Res(menu);
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "메뉴 정렬 수정", description = "id에 해당하는 메뉴 정렬 수정")
    @PutMapping(value ="/sort/{type}/{id}/")
    @AccountValidator
    public ResponseEntity<SingleResult<MenuDto.UpdateReq>> updateSortMenu(@PathVariable("id") String id,
                                                                          @PathVariable("type") String type,
                                                                          @RequestBody MenuDto.UpdateReq req){
        Menu menu = null;
        if (type.equals("m")){
            menu = menuService.updateSortMainMenu(id, req);
        }
        if (type.equals("s")){
            menu = menuService.updateSortSubMenu(id, req);
        }
        MenuDto.UpdateReq res = null;
        return responseService.getSingleResult(res);
    }
}
