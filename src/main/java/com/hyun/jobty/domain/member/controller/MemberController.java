package com.hyun.jobty.domain.member.controller;

import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.conf.swagger.annotation.ApiErrorCode;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.domain.Token;
import com.hyun.jobty.domain.member.domain.TokenType;
import com.hyun.jobty.domain.member.dto.MemberDto;
import com.hyun.jobty.domain.member.dto.TokenRes;
import com.hyun.jobty.domain.member.service.MemberService;
import com.hyun.jobty.domain.member.service.TokenService;
import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.mail.model.Mail;
import com.hyun.jobty.global.mail.model.UrlParam;
import com.hyun.jobty.global.mail.service.MailSenderService;
import com.hyun.jobty.global.response.CommonCode;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
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

import java.util.List;

@Tag(name = "로그인", description = "로그인 API, Last update: 2024.02.22")
@RequestMapping("/")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final ResponseService responseService;
    private final MailSenderService mailSenderService;
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
    @PostMapping("/signin")
    public ResponseEntity<SingleResult<MemberDto.LoginRes>> signin(@RequestBody @Valid MemberDto.LoginReq req){
        // 토큰 생성 및 로그인 정보 가져오기
        // 1. 로그인 정보
        MemberDto member = MemberDto.builder()
                .member(memberService.signin(req))
                .build();
        // 2. 토큰 정보
        TokenRes token = TokenRes.builder()
                .token(tokenService.createToken(req.getId(), TokenType.LOGIN))
                .build();
        MemberDto.LoginRes result = MemberDto.LoginRes.builder()
                .member(member)
                .token(token)
                .build();
        return responseService.getSingleResult(result);
    }

    @Operation(summary = "회원가입", description = "회원가입, 임시 계정 생성 후 해당 ID로 인증 메일 발송(30분동안 유효)")
    @ApiErrorCode(value = ErrorCode.DuplicatedId)
    @PostMapping("/signup")
    public ResponseEntity<SingleResult<String>> signup(@RequestBody @Valid MemberDto.AddMemberReq req){
        // 1. 계정 생성(임시)
        Member member = memberService.signup(req);
        // 2. 토큰 생성 및 메일 전송
        sendMail("signup", tokenService.createToken(member.getId(), TokenType.SIGNUP));
        return responseService.getSingleResult(req.getId());
    }

    /**
     * 메일 전송
     * @param token 토큰
     */
    private void sendMail(String url, Token token){
        mailSenderService.send(
                Mail.builder()
                        .receiverMail(token.getMemberId())
                        .subject("이메일 주소 인증")
                        .url(url)
                        .urlParams(List.of(new UrlParam("id", token.getId()), new UrlParam("token", token.getAccessToken())))
                        .build()
        );
    }

    @Operation(summary = "메일 재발송", description = "임시로 생성한 해당 계정으로 인증 메일 재발송, 이전에 발송한 토큰번호는 유효하지 않음.(재발송된 토큰은 30분동안 유효)")
    @ApiErrorCode(value = {ErrorCode.AccountActivated, ErrorCode.UserNotFound})
    @PostMapping("/mail-resend")
    public ResponseEntity<SingleResult<String>> resendMail(@RequestBody @Valid MemberDto.FindReq req){
        // 1. 계정 생성 여부 확인
        Member member = memberService.findByMemberId(req.getId());
        // 2. 토큰 생성 및 메일 전송
        sendMail("signup", tokenService.createToken(member.getId(), TokenType.SIGNUP));
        return responseService.getSingleResult(req.getId());
    }

    @Operation(summary = "회원가입 이메일 확인", description = "이메일에 발송된 링크 클릭 시 계정 활성화")
    @ApiErrorCode(value = ErrorCode.TokenUserNotFound)
    @GetMapping("signup")
    public ResponseEntity<SingleResult<String>> signupConfirm(@RequestParam String id,
                                                              @RequestParam String token){
        return responseService.getSingleResult(memberService.tokenCheckAndAccountActivate(id, token));
    }

    @Operation(summary = "로그아웃", description = " (토큰 값과 id값 비교 후 실행) id에 해당하는 회원의 토큰을 삭제 후 완료 아이디 리턴(리턴 값은 임시)")
    @GetMapping("signout/{id}")
    @AccountValidator
    public ResponseEntity<SingleResult<String>> signout(@PathVariable("id") String id){
        return responseService.getSingleResult(memberService.signout(id));
    }

    @Operation(summary = "회원탈퇴", description = "(토큰 값과 id값 비교 후 실행) id에 해당하는 회원 탈퇴 후 탈퇴한 아이디 리턴(리턴 값은 임시)")
    @GetMapping("/withdraw/{id}")
    @ApiErrorCode(ErrorCode.UserNotFound)
    @AccountValidator
    public ResponseEntity<SingleResult<String>> withdraw(@PathVariable("id") String id){
        return responseService.getSingleResult(memberService.withdraw(id));
    }

    @GetMapping("/checkId/{id}")
    @Operation(summary = "중복 아이디 확인", description = "id에 해당하는 회원 검색 후 중복 아이디 여부 리턴 및 계정 확인 이메일 전송, 이메일 전송의 경우 비동기로 처리되므로 처리 결과 여부를 알 수 없음")
    @ApiResponse(responseCode = "200", description = "성공(아래 예제의 경우 data에 해당하는 데이터만 출력됩니다.)", content = @Content(schema = @Schema(implementation = MemberDto.Check.class), examples = {
            @ExampleObject(name = "사용 가능", value = "{\"duplicate\": false, \"msg\": \"사용 가능한 아이디입니다.\"}"),
            @ExampleObject(name = "사용 불가", value = "{\"duplicate\": true, \"msg\": \"중복된 아이디입니다.\"}"),
    }))
    public ResponseEntity<SingleResult<MemberDto.Check>> checkId(@PathVariable("id") @Valid
                                                                 MemberDto.FindReq req){
        boolean duplicate = false;
        String msg = CommonCode.AvailableId.getMsg();
        if (memberService.findDuplicateId(req.getId())) {
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

    @Operation(summary = "아이디 찾기", description = "아이디 찾기")
    @PostMapping("find/id")
    public ResponseEntity<SingleResult<MemberDto.FindRes>> findId(@RequestBody @Valid MemberDto.FindReq req){
        String msg = CommonCode.EmailNotFound.getMsg();
        if (memberService.findDuplicateId(req.getId())){
            msg = CommonCode.EmailExists.getMsg();
        }
        MemberDto.FindRes res = MemberDto.FindRes.builder()
                .id(req.getId())
                .msg(msg)
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "계정 비밀번호 찾기", description = "아이디 체크 후 변경 링크 발송")
    @PostMapping("find/pw")
    public ResponseEntity<SingleResult<MemberDto.FindRes>> findPassword(@RequestBody @Valid MemberDto.FindReq req){
        String msg = CommonCode.SendConfirmEMail.getMsg();
        if (!memberService.findDuplicateId(req.getId())){
            msg = CommonCode.EmailNotFound.getMsg();
        }

        // 토큰 생성 및 메일 전송
        sendMail("pw-change", tokenService.createToken(req.getId(), TokenType.FINDPW));

        MemberDto.FindRes res = MemberDto.FindRes.builder()
                .id(req.getId())
                .msg(msg)
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "계정 비밀번호 변경", description = "토큰 값의 ID에 해당하는 비밀번호 변경")
    @PostMapping("pw-change")
    public ResponseEntity<SingleResult<String>> changePassword(@RequestParam String id,
                                                               @RequestParam String token,
                                                               @RequestBody MemberDto.Change req){
        return responseService.getSingleResult(memberService.tokenCheckAndUpdatePassword(id, token, req).getId());
    }
}
