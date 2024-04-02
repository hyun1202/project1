package com.hyun.jobty.domain.setting.detail.service;

import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.domain.setting.detail.dto.SettingDto;
import com.hyun.jobty.global.file.FileVo;

public interface SettingService {
    Setting findByDomain(String domain);
    Setting findById(String id);
    Setting saveDomain(SettingDto.DomainReq req);
    Setting updateDetailSetting(String domain, SettingDto.AddSettingReq req);
    FileVo findByFaviconImage(String id);
}
