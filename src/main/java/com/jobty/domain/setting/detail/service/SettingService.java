package com.jobty.domain.setting.detail.service;

import com.jobty.advice.exception.CustomException;
import com.jobty.advice.exception.ErrorCode;
import com.jobty.domain.setting.detail.domain.Setting;
import com.jobty.domain.setting.detail.dto.SettingDto;
import com.jobty.domain.setting.detail.repository.SettingRepository;
import com.jobty.domain.setting.template.domain.Template;
import com.jobty.global.dto.CheckDto;
import com.jobty.global.response.CommonCode;
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
     * 해당하는 도메인이 존재한지 확인한다.
     * @param domain 도메인
     * @return 도메인 존재 여부
     */
    public boolean existsByDomain(String domain){
        return settingRepository.existsByDomain(domain);
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
     * @param req 업데이트 내용 (이미지 경로, 블로그명, 설명, 키워드)
     * @return 업데이트된 설정 정보
     */
    @Transactional
    public Setting updateDetailSetting(String domain, SettingDto.AddSettingReq req) {
        Setting setting = findByDomain(domain);
        // 설정 정보 업데이트
        setting.updateBlogInfo(req);
        return setting;
    }

    public CheckDto checkDomainDuplicate(SettingDto.FindDomain req){
        boolean duplicate = false;
        String msg = CommonCode.AvailableDomain.getMsg();
        if (existsByDomain(req.getDomain())){
            msg = CommonCode.DuplicatedDomain.getMsg();
            duplicate = true;
        }
        return CheckDto.builder()
                .duplicate(duplicate)
                .msg(msg)
                .build();
    }
}
