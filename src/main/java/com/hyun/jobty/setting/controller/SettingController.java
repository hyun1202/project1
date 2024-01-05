package com.hyun.jobty.setting.controller;

import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.annotation.FileUpload;
import com.hyun.jobty.global.response.ListResult;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.global.util.FileUtil;
import com.hyun.jobty.global.util.FileVo;
import com.hyun.jobty.setting.dto.SettingDto;
import com.hyun.jobty.setting.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/setting/")
public class SettingController {
    private final SettingService settingService;
    private final ResponseService responseService;
    private final FileUtil fileUtil;

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

    @Operation(summary = "썸네일 업로드", description = "블로그 썸네일 업로드")
    @FileUpload(path = "thumbnail")
    @AccountValidator
    @PostMapping(value = "favicon/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ListResult<FileVo> uploadFaviconImage(@PathVariable("id") String id, @ModelAttribute SettingDto.FaviconReq req){
        List<FileVo> files = fileUtil.getFileInfo();
        return responseService.getListResult(files);
    }
}
