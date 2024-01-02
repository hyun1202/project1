package com.hyun.jobty.setting.controller;

import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.setting.dto.SettingDto;
import com.hyun.jobty.setting.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/setting/")
public class SettingController {
    private final SettingService settingService;
    private final ResponseService responseService;

    @AccountValidator
    @GetMapping("/{id}/{domain}")
    public SingleResult<SettingDto.DomainRes> findDomain(@PathVariable("id") String id, @PathVariable("domain") String domain){
        SettingDto.DomainRes res = SettingDto.DomainRes.builder()
                .setting(settingService.findByDomain(domain))
                .build();
        return responseService.getSingleResult(res);
    }

    @AccountValidator
    @PutMapping("/{id}/{domain}")
    public SingleResult<SettingDto.DomainRes> saveDomain(@PathVariable("id") String id, @PathVariable("domain") String domain){
        SettingDto.DomainReq req = new SettingDto.DomainReq(id, domain);
        SettingDto.DomainRes res = SettingDto.DomainRes.builder()
                .setting(settingService.saveDomain(req))
                .build();
        return responseService.getSingleResult(res);
    }

    @AccountValidator
    @PutMapping("/{id}")
    public SingleResult<SettingDto.AddSettingRes> saveSetting(@PathVariable("id") String id, @RequestBody SettingDto.AddSettingReq req){
        SettingDto.AddSettingRes res = SettingDto.AddSettingRes.builder()
                .setting(settingService.updateDetailSetting(id, req))
                .build();
        return responseService.getSingleResult(res);
    }

    @AccountValidator
    @PostMapping(value = "favicon/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<String> uploadFaviconImage(@PathVariable("id") String id, SettingDto.FaviconReq req){
        req.getMultipartFiles();
        return responseService.getSingleResult("h");
    }
}
