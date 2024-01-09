package com.hyun.jobty.setting.service;

import com.hyun.jobty.global.util.FileVo;
import com.hyun.jobty.setting.domain.Setting;
import com.hyun.jobty.setting.dto.SettingDto;

public interface SettingService {
    Setting findByDomain(String domain);
    Setting saveDomain(SettingDto.DomainReq req);
    Setting updateDetailSetting(String id, SettingDto.AddSettingReq req);
    Setting updateFaviconImage(String id, SettingDto.FaviconReq req);
    FileVo findByFaviconImage(String id);
}
