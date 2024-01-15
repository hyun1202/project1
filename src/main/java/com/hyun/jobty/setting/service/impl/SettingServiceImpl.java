package com.hyun.jobty.setting.service.impl;

import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.global.util.FileUtil;
import com.hyun.jobty.global.util.FileVo;
import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.member.service.MemberService;
import com.hyun.jobty.setting.domain.Setting;
import com.hyun.jobty.setting.domain.Template;
import com.hyun.jobty.setting.dto.SettingDto;
import com.hyun.jobty.setting.repository.SettingRepository;
import com.hyun.jobty.setting.service.SettingService;
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
        return settingRepository.findByDomain(domain).orElseThrow(()->new CustomException(ErrorCode.FAIL));
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
        Member member = memberService.findByMemberId(id);
        Setting setting = findByMemberSeq(member.getSeq());
        setting.setFaviconImg(req.getFavicon_img());
        setting.setBlogName(req.getBlog_name());
        setting.setBlogDescription(req.getBlog_description());
        setting.setBlogKeyword(req.getBlog_keyword());
        return setting;
    }

    @Transactional
    @Override
    public Setting updateFaviconImage(String id, SettingDto.FaviconReq req) {
        Member member = memberService.findByMemberId(id);
        Setting setting = findByMemberSeq(member.getSeq());
        setting.setFaviconImg(FileUtil.getSingleFileInfo().saveFilePath());
        return setting;
    }

    @Override
    public FileVo findByFaviconImage(String id){
        Member member = memberService.findByMemberId(id);
        Setting setting = findByMemberSeq(member.getSeq());
        return new FileVo("", "", setting.getFaviconImg(), "");
    }

    private Setting findByMemberSeq(int seq){
        return settingRepository.findByMemberSeq(seq).orElseThrow(()->new CustomException(ErrorCode.FAIL));
    }
}
