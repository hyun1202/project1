package com.hyun.jobty.domain.setting.detail.service.impl;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.service.MemberService;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.domain.setting.detail.dto.SettingDto;
import com.hyun.jobty.domain.setting.detail.repository.SettingRepository;
import com.hyun.jobty.domain.setting.detail.service.SettingService;
import com.hyun.jobty.domain.setting.template.domain.Template;
import com.hyun.jobty.global.file.FileVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;
    private final MemberService memberService;

    @Override
    public Setting findByDomain(String domain){
        return settingRepository.findByDomain(domain).orElseThrow(()->new CustomException(ErrorCode.DomainNotFound));
    }

    @Override
    public Setting findById(String id) {
        return findByMemberUid(id);
    }

    @Override
    public Setting saveDomain(@Valid SettingDto.DomainReq req) {
        Member member = memberService.findByEmail(req.getId());

        // 계정에 도메인 데이터 있는지 확인
        if (settingRepository.existsByMember(member)){
            throw new CustomException(ErrorCode.ExistsDomain);
        }
        // 도메인 중복 확인
        if (settingRepository.findByDomain(req.getDomain()).orElse(null) != null){
            throw new CustomException(ErrorCode.DuplicatedDomain);
        }

        Template template = Template.builder()
                .seq(1) //기본 템플릿 설정
                .build();

        Setting setting = Setting.builder()
                .domain(req.getDomain())
                .member(member)
                .template(template)
                .build();
        return settingRepository.save(setting);
    }

    @Transactional
    @Override
    public Setting updateDetailSetting(String domain, SettingDto.AddSettingReq req) {
        Setting setting = findByDomain(domain);

        setting.setFaviconImg(req.getFavicon_img());
        setting.setBlogName(req.getBlog_name());
        setting.setBlogDescription(req.getBlog_description());
        setting.setBlogKeyword(req.getBlog_keyword());
        return setting;
    }

    @Override
    public FileVo findByFaviconImage(String id){
        Setting setting = findByMemberUid(id);
        return new FileVo("", "", setting.getFaviconImg(), "");
    }

    private Setting findByMemberUid(String id){
        Member member = memberService.findByEmail(id);
        return settingRepository.findByMember_Uid(member.getUid()).orElseThrow(()->new CustomException(ErrorCode.DomainNotFound));
    }
}
