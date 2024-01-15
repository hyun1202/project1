package com.hyun.jobty.member.controller;

import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.global.response.CommonCode;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.global.swagger.annotation.ApiErrorCode;
import com.hyun.jobty.member.dto.MemberDto;
import com.hyun.jobty.member.dto.TokenDto;
import com.hyun.jobty.member.service.MemberService;
import com.hyun.jobty.member.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로그인 컨트롤러", description = "로그인 API")
@RequestMapping("/")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final ResponseService responseService;
    private final TokenService tokenService;

    @Operation(summary = "계정 정보 확인", description = "로그인 계정에 해당하는 계정 정보 리턴 (토큰 값과 id값 비교 후 실행)")
    @GetMapping("/user/{id}")
    @AccountValidator
    public ResponseEntity<SingleResult<MemberDto>> getMember(@PathVariable("id") String id){
        MemberDto member = MemberDto.builder().member(memberService.findByMemberId(id)).build();
        return responseService.getSingleResult(member);
    }

    @Operation(summary = "로그인", description = "id, pw를 입력하여 로그인, 성공 시 토큰 값 리턴")
    @ApiErrorCode(value = {ErrorCode.UserNotFound, ErrorCode.IncorrectPassword})
    @PostMapping("/login")
    public ResponseEntity<SingleResult<MemberDto.LoginRes>> login(@RequestBody @Valid MemberDto.LoginReq req){
        // 토큰 생성 및 로그인 정보 가져오기
        // 1. 로그인 정보
        MemberDto member = MemberDto.builder()
                .member(memberService.login(req))
                .build();
        // 2. 토큰 정보
        TokenDto token = TokenDto.builder()
                .token(tokenService.createAccessToken(req.getId()))
                .build();
        MemberDto.LoginRes result = MemberDto.LoginRes.builder()
                .member(member)
                .token(token)
                .build();
        return responseService.getSingleResult(result);
    }

    @Operation(summary = "회원가입", description = "회원가입, 성공 시 가입된 아이디 리턴")
    @ApiErrorCode(value = ErrorCode.DuplicatedId)
    @PostMapping("/signup")
    public ResponseEntity<SingleResult<String>> signup(@RequestBody @Valid MemberDto.AddMemberReq req){
        return responseService.getSingleResult(memberService.save(req));
    }

    @Operation(summary = "로그아웃", description = " (토큰 값과 id값 비교 후 실행) id에 해당하는 회원의 토큰을 삭제 후 완료 아이디 리턴(리턴 값은 임시)")
    @GetMapping("logout/{id}")
    @AccountValidator
    public ResponseEntity<SingleResult<String>> logout(@PathVariable("id") String id){
        tokenService.deleteToken(id);
        return responseService.getSingleResult(id);
    }

    @Operation(summary = "회원탈퇴", description = "(토큰 값과 id값 비교 후 실행) id에 해당하는 회원 탈퇴 후 탈퇴한 아이디 리턴(리턴 값은 임시)")
    @GetMapping("/withdraw/{id}")
    @ApiErrorCode(ErrorCode.UserNotFound)
    @AccountValidator
    public ResponseEntity<SingleResult<String>> withdraw(@PathVariable("id") String id){
        return responseService.getSingleResult(memberService.withdraw(id));
    }

    @GetMapping("/checkId/{id}")
    @Operation(summary = "중복 아이디 확인", description = "id에 해당하는 회원 검색 후 중복 아이디 여부 리턴")
    @ApiResponse(responseCode = "200", description = "성공(아래 예제의 경우 data에 해당하는 데이터만 출력됩니다.)", content = @Content(schema = @Schema(implementation = MemberDto.Check.class), examples = {
            @ExampleObject(name = "사용 가능", value = "{\"duplicate\": false, \"msg\": \"사용 가능한 아이디입니다.\"}"),
            @ExampleObject(name = "사용 불가", value = "{\"duplicate\": true, \"msg\": \"중복된 아이디입니다.\"}"),
    }))
    public ResponseEntity<SingleResult<MemberDto.Check>> checkId(@PathVariable("id") String id){
        boolean duplicate = false;
        String msg = CommonCode.AvailableId.getMsg();
        if (memberService.findDuplicateId(id)) {
            // 아이디 중복
            duplicate = true;
            msg = CommonCode.DuplicatedId.getMsg();
        }
        MemberDto.Check check = MemberDto.Check.builder()
                .duplicate(duplicate)
                .msg(msg)
                .build();
        return responseService.getSingleResult(check);
    }
}
