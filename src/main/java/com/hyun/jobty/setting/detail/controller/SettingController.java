package com.hyun.jobty.setting.detail.controller;

import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.annotation.FileUpload;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.setting.detail.dto.SettingDto;
import com.hyun.jobty.setting.detail.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/setting/")
public class SettingController {
    private final SettingService settingService;
    private final ResponseService responseService;

    @AccountValidator
    @GetMapping("/{id}/{domain}")
    public ResponseEntity<SingleResult<SettingDto.DomainRes>> findDomain(@PathVariable("id") String id, @PathVariable("domain") String domain){
        SettingDto.DomainRes res = SettingDto.DomainRes.builder()
                .setting(settingService.findBySetting(domain))
                .build();
        return responseService.getSingleResult(res);
    }

    @AccountValidator
    @PutMapping("/{id}/{domain}")
    public ResponseEntity<SingleResult<SettingDto.DomainRes>> saveDomain(@PathVariable("id") String id, @PathVariable("domain") String domain){
        SettingDto.DomainReq req = new SettingDto.DomainReq(id, domain);
        SettingDto.DomainRes res = SettingDto.DomainRes.builder()
                .setting(settingService.saveDomain(req))
                .build();
        return responseService.getSingleResult(res);
    }

    @AccountValidator
    @PutMapping("/{id}")
    public ResponseEntity<SingleResult<SettingDto.AddSettingRes>> saveSetting(@PathVariable("id") String id, @RequestBody SettingDto.AddSettingReq req){
        SettingDto.AddSettingRes res = SettingDto.AddSettingRes.builder()
                .setting(settingService.updateDetailSetting(id, req))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "썸네일 업로드", description = "블로그 썸네일 업로드")
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
