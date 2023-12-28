package com.hyun.jobty.member.controller;

import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.global.response.ListResult;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.member.dto.MemberDto;
import com.hyun.jobty.member.dto.TokenDto;
import com.hyun.jobty.member.service.MemberService;
import com.hyun.jobty.member.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "로그인 컨트롤러", description = "로그인 API입니다.")
@RequestMapping("/")
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

    @Operation(summary = "로그인", description = "로그인입니다.")
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

    @GetMapping("logout/{id}")
    public SingleResult<String> logout(@PathVariable("id") String id){
        tokenService.deleteToken(id);
        String msg = "로그아웃이 완료되었습니다.";
        return responseService.getSingleResult(msg);
    }

    @GetMapping("/withdraw/{id}")
    public SingleResult<String> withdraw(@PathVariable("id") String id){
        return responseService.getSingleResult(memberService.withdraw(id));
    }

    @GetMapping("/checkId/{id}")
    public SingleResult<MemberDto.Check> checkId(@PathVariable("id") String id){
        boolean isDuplicate = false;
        String msg = "사용 가능한 아이디입니다.";
        if (memberService.findDuplicateId(id)) {
            isDuplicate = true;
            msg = ErrorCode.DuplicatedId.getMsg();
        }
        MemberDto.Check check = MemberDto.Check.builder()
                .isDuplicate(isDuplicate)
                .msg(msg)
                .build();
        return responseService.getSingleResult(check);
    }
}
