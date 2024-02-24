package com.hyun.jobty.domain.setting.detail.controller;

import com.hyun.jobty.domain.setting.detail.service.SettingService;
import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.annotation.FileUpload;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.domain.setting.detail.dto.SettingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "설정 컨트롤러", description = "도메인 등 정보 설정을 위한 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/setting/")
public class SettingController {
    private final SettingService settingService;
    private final ResponseService responseService;

    @Operation(summary = "도메인 조회", description = "id로 된 도메인을 조회합니다.")
    @AccountValidator
    @GetMapping("/{id}/{domain}")
    public ResponseEntity<SingleResult<SettingDto.DomainRes>> findDomain(@PathVariable("id") String id, @PathVariable("domain") String domain){
        SettingDto.DomainRes res = SettingDto.DomainRes.builder()
                .setting(settingService.findBySetting(domain))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "도메인 저장", description = "id에 해당 도메인을 저장합니다.")
    @AccountValidator
    @PutMapping("/{id}/{domain}")
    public ResponseEntity<SingleResult<SettingDto.DomainRes>> saveDomain(@PathVariable("id") String id, @PathVariable("domain") String domain){
        SettingDto.DomainReq req = new SettingDto.DomainReq(id, domain);
        SettingDto.DomainRes res = SettingDto.DomainRes.builder()
                .setting(settingService.saveDomain(req))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "설정 저장", description = "id에 해당 설정을 저장합니다.")
    @AccountValidator
    @PutMapping("/{id}")
    public ResponseEntity<SingleResult<SettingDto.AddSettingRes>> saveSetting(@PathVariable("id") String id, @RequestBody SettingDto.AddSettingReq req){
        SettingDto.AddSettingRes res = SettingDto.AddSettingRes.builder()
                .setting(settingService.updateDetailSetting(id, req))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "썸네일 업로드(테스트)", description = "블로그 썸네일 업로드입니다.")
    @FileUpload(path = "thumbnail")
    @AccountValidator
    @PostMapping(value = "favicon/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleResult<SettingDto.FaviconRes>> uploadFaviconImage(@PathVariable("id") String id, @ModelAttribute SettingDto.FaviconReq req){
        SettingDto.FaviconRes res = SettingDto.FaviconRes.builder()
                .setting(settingService.updateFaviconImage(id, req))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "썸네일 다운로드", description = "블로그 썸네일 다운로드")
    @AccountValidator
    @GetMapping(value = "favicon/{id}")
    public ResponseEntity<Resource> downloadFaviconImage(@PathVariable("id") String id){
        return responseService.getFileResponseEntity(settingService.findByFaviconImage(id));
    }
}
