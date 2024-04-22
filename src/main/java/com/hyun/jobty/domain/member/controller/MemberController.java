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
import com.hyun.jobty.global.accountValidator.annotation.AccountValidator;
import com.hyun.jobty.global.mail.model.Mail;
import com.hyun.jobty.global.mail.model.UrlParam;
import com.hyun.jobty.global.mail.service.MailSenderService;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.ElementType;
import java.util.List;

@Tag(name = "로그인", description = "로그인 API, Last update: 2024.04.15 id 관련 http메서드 및 json 데이터 변경")
@RequestMapping("/")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final ResponseService responseService;
    private final MailSenderService mailSenderService;
    private final TokenService tokenService;

    @Operation(summary = "계정 정보 확인(테스트)", description = "로그인 계정에 해당하는 계정 정보 리턴 (토큰 값과 id값 비교 후 실행)")
    @GetMapping("/account/user")
    @AccountValidator(type = ElementType.PARAMETER)
    public ResponseEntity<SingleResult<MemberDto>> getMember(@RequestParam String id){
        MemberDto member = MemberDto.builder().member(memberService.findByEmail(id)).build();
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
                .token(tokenService.createJwtToken(member))
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
        sendMail(TokenType.signup.getUrl(), tokenService.createToken(member.getEmail(), TokenType.signup));
        return responseService.getSingleResult(req.getEmail());
    }

    /**
     * 메일 전송
     * @param token 토큰
     */
    private void sendMail(String url, Token token){
        mailSenderService.send(
                Mail.builder()
                        .receiverMail(token.getEmail())
                        .subject("이메일 주소 인증")
                        .url(url)
                        .urlParams(List.of(new UrlParam("token_id", token.getId()), new UrlParam("token", token.getAccessToken())))
                        .build()
        );
    }

    @Operation(summary = "메일 재발송", description = "임시로 생성한 해당 계정으로 인증 메일 재발송, 이전에 발송한 토큰번호는 유효하지 않음.(재발송된 토큰은 30분동안 유효), 메일 발송의 경우 비동기로 진행하므로 항상 성공됨(프론트에서 확인 불가)")
    @ApiErrorCode(value = {ErrorCode.TokenUserNotFound, ErrorCode.AccountActivated, ErrorCode.UserNotFound})
    @PostMapping("/account/mail-resend")
    public ResponseEntity<SingleResult<String>> resendMail(@RequestParam @NotNull TokenType type,
                                                           @RequestBody @Valid MemberDto.FindReq req){
        // 1.메일 발송 여부 확인 (토큰 확인)
        // TODO 이전에 발송된 회원가입 주소는 왜 클릭이 되는가...?
        memberService.checkAccountStatus(req.getEmail(), type);
        // 2. 토큰 생성 및 메일 전송
        sendMail(type.getUrl(), tokenService.createToken(req.getEmail(), type));
        return responseService.getSingleResult(req.getEmail());
    }

    @Operation(summary = "회원가입 이메일 확인", description = "이메일에 발송된 링크 클릭 시 계정 활성화")
    @ApiErrorCode(value = ErrorCode.TokenUserNotFound)
    @GetMapping("signup")
    public ResponseEntity<SingleResult<String>> signupConfirm(@RequestParam String token_id,
                                                              @RequestParam String token){
        return responseService.getSingleResult(memberService.tokenCheckAndAccountActivate(token_id, token));
    }

    @Operation(summary = "로그아웃", description = " (토큰 값과 id값 비교 후 실행) id에 해당하는 회원의 토큰을 삭제 후 완료 아이디 리턴(리턴 값은 임시)")
    @PostMapping("signout")
    @AccountValidator
    public ResponseEntity<SingleResult<String>> signout(@RequestBody MemberDto.FindReq req){
        return responseService.getSingleResult(memberService.signout(req.getEmail()));
    }

    @Operation(summary = "회원탈퇴", description = "(토큰 값과 id값 비교 후 실행) id에 해당하는 회원 탈퇴 후 탈퇴한 아이디 리턴(리턴 값은 임시)")
    @PostMapping("/account/withdraw")
    @ApiErrorCode(ErrorCode.UserNotFound)
    @AccountValidator
    public ResponseEntity<SingleResult<String>> withdraw(@RequestBody MemberDto.FindReq req){
        return responseService.getSingleResult(memberService.withdraw(req.getEmail()));
    }

    @PostMapping("/account/check/id")
    @Operation(summary = "중복 아이디 확인", description = "id에 해당하는 회원 검색 후 중복 아이디 여부 리턴 및 계정 확인")
    public ResponseEntity<SingleResult<MemberDto.Check>> checkId(@RequestBody @Valid
                                                                 MemberDto.FindReq req){
        return responseService.getSingleResult(memberService.checkDuplicateId(req.getEmail()));
    }

    @Operation(summary = "아이디 찾기", description = "아이디 찾기")
    @ApiErrorCode(ErrorCode.UserNotFound)
    @PostMapping("/account/find/id")
    public ResponseEntity<SingleResult<MemberDto.FindRes>> findId(@RequestBody @Valid MemberDto.FindReq req){
        return responseService.getSingleResult(memberService.findAccountId(req.getEmail()));
    }

    @Operation(summary = "계정 비밀번호 찾기", description = "아이디 확인 후 해당 이메일로 변경 링크 메일 발송, 메일 발송의 경우 비동기로 진행하므로 항상 성공됨(프론트에서 확인 불가)")
    @ApiErrorCode(ErrorCode.UserNotFound)
    @PostMapping("/account/find/pwd")
    public ResponseEntity<SingleResult<MemberDto.FindRes>> findPassword(@RequestBody @Valid MemberDto.FindReq req){
        MemberDto.FindRes res = memberService.findAccountPwd(req.getEmail());
        // 토큰 생성 및 메일 전송
        sendMail("account/change/pwd", tokenService.createToken(res.getEmail(), TokenType.change));
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "계정 비밀번호 변경", description = "토큰 값의 ID에 해당하는 비밀번호 변경, 토큰 사용 후 재사용 불가")
    @ApiErrorCode({ErrorCode.TokenUserNotFound, ErrorCode.UnexpectedToken, ErrorCode.UserNotFound})
    @PostMapping("/account/change/pwd")
    public ResponseEntity<SingleResult<String>> changePassword(@RequestParam String token_id,
                                                               @RequestParam String token,
                                                               @RequestBody MemberDto.Change req){
        return responseService.getSingleResult(memberService.tokenCheckAndUpdatePassword(token_id, token, req).getEmail());
    }
}
