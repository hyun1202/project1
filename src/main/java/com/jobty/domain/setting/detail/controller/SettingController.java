package com.jobty.domain.setting.detail.controller;

import com.jobty.advice.exception.ErrorCode;
import com.jobty.conf.swagger.annotation.ApiErrorCode;
import com.jobty.domain.setting.detail.dto.SettingDto;
import com.jobty.domain.setting.detail.service.SettingService;
import com.jobty.global.accountValidator.annotation.AccountValidator;
import com.jobty.global.dto.CheckDto;
import com.jobty.global.response.ResponseService;
import com.jobty.global.response.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.ElementType;

@Tag(name = "설정 컨트롤러", description = "도메인 등 정보 설정 및 조회 - 도메인 조회 url변경, Last Update 2024.04.24")
@RestController
@RequiredArgsConstructor
@RequestMapping("/setting/")
public class SettingController {
    private final SettingService settingService;
    private final ResponseService responseService;

    @Operation(summary = "설정 정보 조회", description = "uid로 된 도메인을 조회합니다.(uid와 토큰의 uid를 확인)")
    @AccountValidator(type = ElementType.PARAMETER)
    @ApiErrorCode(ErrorCode.DomainNotFound)
    @GetMapping("/domain")
    public ResponseEntity<SingleResult<SettingDto>> findDomainById(@RequestParam("uid") String uid){
        return responseService.getSingleResult(
                SettingDto.builder()
                        .setting(settingService.findByMemberUid(uid))
                        .build()
        );
    }

    @Operation(summary = "도메인 저장", description = "해당 uid에 도메인 정보를 저장합니다.")
    @AccountValidator
    @PostMapping("/domain")
    public ResponseEntity<SingleResult<SettingDto.DomainRes>> saveDomain(@RequestBody SettingDto.DomainReq req){
        SettingDto.DomainRes res = SettingDto.DomainRes.builder()
                .setting(settingService.saveDomain(req))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "설정 저장", description = "id에 설정을 저장합니다.(썸네일 이미지의 경우 업로드 완료 후 리턴된 썸네일 이미지 경로 입력)")
    @AccountValidator
    @PostMapping("/{domain}/")
    public ResponseEntity<SingleResult<SettingDto.AddSettingRes>> saveSetting(@PathVariable("domain") String domain,
                                                                              @RequestBody SettingDto.AddSettingReq req){
        SettingDto.AddSettingRes res = SettingDto.AddSettingRes.builder()
                .setting(settingService.updateDetailSetting(domain, req))
                .build();
        return responseService.getSingleResult(res);
    }
    @PostMapping("/domain/check")
    public ResponseEntity<SingleResult<CheckDto>> checkDomain(@RequestBody SettingDto.FindDomain req){
        return responseService.getSingleResult(settingService.checkDomainDuplicate(req));
    }
}
