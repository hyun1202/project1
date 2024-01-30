package com.hyun.jobty.setting.detail.service;

import com.hyun.jobty.global.util.FileVo;
import com.hyun.jobty.setting.detail.dto.SettingDto;
import com.hyun.jobty.setting.detail.domain.Setting;

public interface SettingService {
    Setting findBySetting(String domain);
    String findByDomain(String id);
    Setting saveDomain(SettingDto.DomainReq req);
    Setting updateDetailSetting(String id, SettingDto.AddSettingReq req);
    Setting updateFaviconImage(String id, SettingDto.FaviconReq req);
    FileVo findByFaviconImage(String id);
}
