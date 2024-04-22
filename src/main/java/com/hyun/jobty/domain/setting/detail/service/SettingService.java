package com.hyun.jobty.domain.setting.detail.service;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.domain.setting.detail.dto.SettingDto;
import com.hyun.jobty.domain.setting.detail.repository.SettingRepository;
import com.hyun.jobty.domain.setting.template.domain.Template;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final SettingRepository settingRepository;

    /**
     * 도메인으로 설정 정보를 찾는다.
     * @param domain 도메인
     * @return 설정 정보
     * @exception CustomException ErrorCode.DomainNotFound
     */
    public Setting findByDomain(String domain){
        return settingRepository.findByDomain(domain).orElseThrow(() -> new CustomException(ErrorCode.DomainNotFound));
    }

    /**
     * uid로 설정 정보를 찾는다.
     * @param uid uid
     * @return 설정 정보
     * @exception CustomException ErrorCode.DomainNotFound
     */
    public Setting findByMemberUid(String uid){
        return settingRepository.findByMember_Uid(uid).orElseThrow(() -> new CustomException(ErrorCode.DomainNotFound));
    }

    /**
     * 도메인 정보를 저장한다.
     * 기본 템플릿에 해당하는 데이터가 반드시 있어야 저장 가능
     * @param req 유저, 도메인 정보
     * @return 저장된 도메인 정보
     */
    public Setting saveDomain(@Valid SettingDto.DomainReq req) {
        // 계정에 도메인 데이터 있는지 확인
        if (settingRepository.existsByMember_Uid(req.getUid())){
            throw new CustomException(ErrorCode.ExistsDomain);
        }
        // 도메인 중복 확인
        if (settingRepository.findByDomain(req.getDomain()).orElse(null) != null){
            throw new CustomException(ErrorCode.DuplicatedDomain);
        }

        // TODO 템플릿 정보 있는지 확인 없다면 예외처리
        Template template = Template.builder()
                .seq(1) //기본 템플릿 설정
                .build();

        Setting setting = Setting.builder()
                .domain(req.getDomain())
                .member(req.uidToEntity())
                .template(template)
                .build();

        return settingRepository.save(setting);
    }

    /**
     * 설정 정보를 업데이트 한다
     * @param domain 도메인
     * @param req 업데이트 내용 (이미지 경로, 블로그명, 블로그 설명, 키워드)
     * @return 업데이트된 설정 정보
     */
    @Transactional
    public Setting updateDetailSetting(String domain, SettingDto.AddSettingReq req) {
        Setting setting = findByDomain(domain);

        setting.setFaviconImg(req.getFavicon_img());
        setting.setBlogName(req.getBlog_name());
        setting.setBlogDescription(req.getBlog_description());
        setting.setBlogKeyword(req.getBlog_keyword());
        return setting;
    }
}
