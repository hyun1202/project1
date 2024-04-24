package com.hyun.jobty.domain.setting.menu.controller;

import com.hyun.jobty.domain.setting.menu.domain.Menu;
import com.hyun.jobty.domain.setting.menu.dto.MenuDto;
import com.hyun.jobty.domain.setting.menu.service.MenuService;
import com.hyun.jobty.global.accountValidator.annotation.AccountValidator;
import com.hyun.jobty.global.accountValidator.dto.ValidatorDto;
import com.hyun.jobty.global.response.ListResult;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "메뉴 컨트롤러", description = "메뉴 API *** group_no: 대메뉴 정렬 값, sort_no: 소메뉴 정렬 값 *** Last update: 2024.03.28")
@RequiredArgsConstructor
@RequestMapping("setting/menu/")
@RestController
// TODO id관련은 모두 domain으로 변경
public class MenuController {
    private final MenuService menuService;
    private final ResponseService responseService;

    @Operation(summary = "메뉴 조회", description = "도메인에 해당하는 메뉴 조회")
    @GetMapping(value ="/{domain}")
    @AccountValidator(value = "dto")
    public ResponseEntity<ListResult<MenuDto.ListRes>> getMenu(@PathVariable("domain") String domain,
                                                            ValidatorDto dto){
        List<Menu> menu = menuService.findMenuByDomain(domain);
        List<MenuDto.ListRes> res = menu.stream().map(MenuDto.ListRes::new).toList();
        return responseService.getListResult(res);
    }

    @Operation(summary = "메뉴 단건 저장", description = "도메인에 해당하는 메뉴 저장")
    @PostMapping(value ="/{domain}")
    @AccountValidator
    public ResponseEntity<SingleResult<MenuDto.Res>> saveMenu(@PathVariable("domain") String domain,
                                                              @RequestBody MenuDto.Req req){
        Menu menu = menuService.saveSingleMenu(domain, req);
        MenuDto.Res res = new MenuDto.Res(menu);
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "메뉴 단건 삭제", description = "도메인에 해당하는 메뉴 삭제")
    @DeleteMapping(value ="/{domain}/{menu_id}")
    @AccountValidator(value = "dto")
    public ResponseEntity<SingleResult<Long>> removeMenu(@PathVariable("domain") String domain,
                                                            @PathVariable("menu_id") int menu_id,
                                                            ValidatorDto dto){
        return responseService.getSingleResult(menuService.deleteSingleMenu(domain, menu_id));
    }

    @Operation(summary = "메뉴 단건 수정", description = "도메인에 해당하는 메뉴 업데이트(정렬 X)")
    @PutMapping(value ="/{domain}/")
    @AccountValidator
    public ResponseEntity<SingleResult<MenuDto.Res>> updateMenu(@PathVariable("domain") String domain,
                                                                @RequestBody MenuDto.UpdateReq req){
        Menu menu = menuService.updateSingleMenu(domain, req);
        MenuDto.Res res = new MenuDto.Res(menu);
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "메뉴 정렬 수정", description = "도메인에 해당하는 메뉴 정렬 수정")
    @PutMapping(value ="/sort/{domain}")
    @AccountValidator
    public ResponseEntity<ListResult<MenuDto.UpdateSort.Data>> updateSortMenu(@PathVariable String domain,
                                                                          @RequestBody MenuDto.UpdateSort req){
        List<MenuDto.UpdateSort.Data> res = menuService.updateSortMenu(domain, req)
                .stream()
                .map(MenuDto.UpdateSort.Data::new)
                .toList();
        return responseService.getListResult(res);
    }
}
