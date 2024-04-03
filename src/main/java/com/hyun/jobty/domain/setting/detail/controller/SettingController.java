package com.hyun.jobty.domain.setting.detail.controller;

import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.conf.swagger.annotation.ApiErrorCode;
import com.hyun.jobty.domain.setting.detail.dto.SettingDto;
import com.hyun.jobty.domain.setting.detail.service.SettingService;
import com.hyun.jobty.global.accountValidator.annotation.AccountValidator;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.ElementType;

@Tag(name = "설정 컨트롤러", description = "도메인 등 정보 설정을 위한 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/setting/")
public class SettingController {
    private final SettingService settingService;
    private final ResponseService responseService;

    @Operation(summary = "도메인 조회", description = "id로 된 도메인을 조회합니다.")
    @AccountValidator(type = ElementType.PARAMETER)
    @ApiErrorCode(ErrorCode.DomainNotFound)
    @GetMapping("/domain")
    public ResponseEntity<SingleResult<SettingDto>> findDomainById(@RequestParam String id){
        return responseService.getSingleResult(
                SettingDto.builder()
                        .setting(settingService.findById(id))
                        .build()
        );
    }

    @Operation(summary = "도메인 저장", description = "id에 해당 도메인을 저장합니다.")
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
}
