package com.hyun.jobty.domain.setting.detail.service.impl;

import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.util.file.FileUtil;
import com.hyun.jobty.util.file.FileVo;
import com.hyun.jobty.domain.member.service.MemberService;
import com.hyun.jobty.domain.setting.template.domain.Template;
import com.hyun.jobty.domain.setting.detail.dto.SettingDto;
import com.hyun.jobty.domain.setting.detail.repository.SettingRepository;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.domain.setting.detail.service.SettingService;
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
    public Setting findBySetting(String domain){
        return settingRepository.findByDomain(domain).orElseThrow(()->new CustomException(ErrorCode.FAIL));
    }

    @Override
    public String findByDomain(String id) {
        return findByMemberSeq(id).getDomain();
    }

    @Override
    public Setting saveDomain(@Valid SettingDto.DomainReq req) {
        Member member = memberService.findByMemberId(req.getId());

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
    public Setting updateDetailSetting(String id, SettingDto.AddSettingReq req) {
        Setting setting = findByMemberSeq(id);
        setting.setFaviconImg(req.getFavicon_img());
        setting.setBlogName(req.getBlog_name());
        setting.setBlogDescription(req.getBlog_description());
        setting.setBlogKeyword(req.getBlog_keyword());
        return setting;
    }

    @Transactional
    @Override
    public Setting updateFaviconImage(String id, SettingDto.FaviconReq req) {
        Setting setting = findByMemberSeq(id);
        setting.setFaviconImg(FileUtil.getSingleFileInfo().saveFilePath());
        return setting;
    }

    @Override
    public FileVo findByFaviconImage(String id){
        Setting setting = findByMemberSeq(id);
        return new FileVo("", "", setting.getFaviconImg(), "");
    }

    private Setting findByMemberSeq(String id){
        Member member = memberService.findByMemberId(id);
        return settingRepository.findByMemberSeq(member.getSeq()).orElseThrow(()->new CustomException(ErrorCode.FAIL));
    }
}
