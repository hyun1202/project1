package com.hyun.jobty.domain.setting.detail.service;

import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.util.file.FileVo;
import com.hyun.jobty.domain.setting.detail.dto.SettingDto;

public interface SettingService {
    Setting findBySetting(String domain);
    String findByDomain(String id);
    Setting saveDomain(SettingDto.DomainReq req);
    Setting updateDetailSetting(String id, SettingDto.AddSettingReq req);
    Setting updateFaviconImage(String id, SettingDto.FaviconReq req);
    FileVo findByFaviconImage(String id);
}
