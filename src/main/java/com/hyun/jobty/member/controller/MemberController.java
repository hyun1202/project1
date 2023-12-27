package com.hyun.jobty.member.controller;

import com.hyun.jobty.global.response.ListResult;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.global.security.jwt.TokenProvider;
import com.hyun.jobty.member.dto.MemberDto;
import com.hyun.jobty.member.dto.TokenDto;
import com.hyun.jobty.member.service.MemberService;
import com.hyun.jobty.member.service.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "로그인 컨트롤러", description = "로그인 API입니다.")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final ResponseService responseService;
    private final TokenService tokenService;


    @GetMapping("/user/{id}")
    public SingleResult<MemberDto.Response> getMember(@PathVariable("id") String id){
        MemberDto.Response member = MemberDto.Response.builder().member(memberService.findByMemberId(id)).build();
        return responseService.getSingleResult(member);
    }


    @PostMapping("/login")
    public ListResult<MemberDto.Response> login(@RequestBody @Valid MemberDto.LoginRequest req){
        // 토큰 생성 및 로그인 정보 가져오기
        // 1. 로그인 정보
        MemberDto.Response member = MemberDto.Response.builder()
                .member(memberService.login(req)).build();
        // 2. 토큰 정보
        TokenDto token = TokenDto.builder()
                .token(tokenService.createAccessToken(req.getId()))
                .build();
        return responseService.getListResult("member", member, "token", token);
    }

    @PostMapping("/signup")
    public SingleResult<String> signup(@RequestBody @Valid MemberDto.AddMemberRequest req){
        return responseService.getSingleResult(memberService.save(req));
    }
}
