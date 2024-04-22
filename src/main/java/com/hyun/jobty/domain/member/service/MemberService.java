package com.hyun.jobty.domain.member.service;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.domain.Role;
import com.hyun.jobty.domain.member.domain.Status;
import com.hyun.jobty.domain.member.domain.TokenType;
import com.hyun.jobty.domain.member.dto.MemberDto;
import com.hyun.jobty.domain.member.repository.MemberRepository;
import com.hyun.jobty.global.response.CommonCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원 식별 번호로 회원 정보를 찾는다.
     * @param uid 회원 식별 번호
     * @return 회원 정보
     * @exception CustomException {@link ErrorCode} UserNotFound
     */
    
    public Member findByMemberUid(String uid) {
        return memberRepository.findById(uid).orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
    }

    /**
     * 회원 아이디(이메일)로 회원 정보를 찾는다.
     * @param email 회원 이메일
     * @return 회원 정보
     * @exception CustomException {@link ErrorCode} UserNotFound
     */
    
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
    }

    /**
     * 회원 아이디 중복 여부를 확인한다.
     * @param id 회원 아이디
     * @return 회원 아이디 중복 여부
     */
    
    public boolean findDuplicateId(String id){
        return memberRepository.existsByEmail(id);
    }

    /**
     * 메일 재전송을 위해 계정 인증 상태를 확인한다. 인증 상태라면 예외를 발생한다.
     * @param email 회원 이메일
     * @return 회원 정보
     * @exception CustomException {@link ErrorCode} AccountActivated 계정 인증 상태
     */
    
    public Member checkAccountStatus(String email, TokenType type) {
        Member member = findByEmail(email);
        // 계정 인증 여부 확인
        if (!member.isTemporaryAccount(member.getStatus()))
            throw new CustomException(ErrorCode.AccountActivated);
        // 토큰 생성 여부 확인
        tokenService.checkByTokenId(email, type);
        return member;
    }

    /**
     * 계정 아이디 찾기
     * @param member_id 유저 이메일
     */
    
    public MemberDto.FindRes findAccountId(String member_id) {
        String msg = CommonCode.EmailNotFound.getMsg();
        // 중복일 경우
        if (findDuplicateId(member_id)){
            msg = CommonCode.EmailExists.getMsg();
        }
        return MemberDto.FindRes.builder()
                .email(member_id)
                .msg(msg)
                .build();
    }

    /**
     * 계정 비밀번호 찾기
     * @param member_id 유저 이메일
     */
    
    public MemberDto.FindRes findAccountPwd(String member_id) {
        String msg = CommonCode.SendConfirmEMail.getMsg();
        findByEmail(member_id);
        return MemberDto.FindRes.builder()
                .email(member_id)
                .msg(msg)
                .build();
    }

    /**
     * 아이디 중복 체크
     * @param member_id
     */
    
    public MemberDto.Check checkDuplicateId(String member_id) {
        boolean duplicate = false;
        String msg = CommonCode.AvailableId.getMsg();
        if (findDuplicateId(member_id)) {
            // 아이디 중복
            duplicate = true;
            msg = CommonCode.DuplicatedId.getMsg();
        }
        return MemberDto.Check.builder()
                .duplicate(duplicate)
                .msg(msg)
                .build();
    }

    /**
     * 토큰 생성 여부 확인
     * @param member_id 토큰 아이디
     * @param type 토큰 타입
     */
    
    public void checkTokenAndAccountId(String member_id, TokenType type) {
        tokenService.checkByTokenId(member_id, type);
    }

    /**
     * 비밀번호 변경을 위해 토큰 체크 및 비밀번호 변경
     * @param token_id 아이디
     * @param token 토큰
     * @param req 변경할 비밀번호 내용
     * @return 변경 완료한 회원 정보
     * @exception CustomException {@link ErrorCode} UnexpectedToken 토큰 검증 실패
     */
    
    @Transactional
    public Member tokenCheckAndUpdatePassword(String token_id, String token, MemberDto.Change req){
        // 토큰 검증
        if (!tokenService.validToken(token_id, token, TokenType.change))
            throw new CustomException(ErrorCode.UnexpectedToken);
        Member member = findByEmail(tokenService.findByTokenId(token_id).getEmail());
        // 비밀번호 업데이트
        member.updatePassword(bCryptPasswordEncoder.encode(req.getPwd()));
        // 사용한 토큰은 재사용 못하도록 삭제
        tokenService.deleteToken(token_id);
        return member;
    }

    /**
     * 인증이 완료된 계정 활성화를 위해 토큰 체크 및 계정 활성화
     * @param token_id 토큰 아이디
     * @param token 토큰
     * @return 활성화한 계정 아이디
     * @exception CustomException {@link ErrorCode} TokenUserNotFound 토큰 오류
     */
    
    @Transactional
    public String tokenCheckAndAccountActivate(String token_id, String token) {
        // 토큰 검증
        if (!tokenService.validToken(token_id, token, TokenType.signup))
            throw new CustomException(ErrorCode.TokenUserNotFound);
        // 토큰에 해당하는 회원 가져오기
        Member member = findByEmail(tokenService.findByTokenId(token_id).getEmail());
        // 계정 활성화
        member.memberActivate();
        // 토큰 확인 완료하였으므로 토큰 삭제
        tokenService.deleteToken(token_id);
        return member.getEmail();
    }

    /**
     * 회원 로그인
     * @param loginReq 로그인 정보
     * @return 로그인한 회원 정보
     * @exception CustomException {@link ErrorCode} IncorrectPassword 비밀번호 오류
     * @exception CustomException {@link ErrorCode} AccountDisabled 만료 계정
     * @exception CustomException {@link ErrorCode} AccountExpired 탈퇴 계정
     */
    
    @Transactional
    public Member signin(MemberDto.LoginReq loginReq) {
        // id 체크 로직
        Member member = memberRepository.findByEmail(loginReq.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.LoginFailed));
        // pw 체크 로직
        if (!bCryptPasswordEncoder.matches(loginReq.getPwd(), member.getPwd())){
            throw new CustomException(ErrorCode.IncorrectPassword);
        }
        // 임시 계정 여부 확인
        if (member.getStatus() == Status.TEMPORARY.ordinal())
            throw new CustomException(ErrorCode.NotActivatedAccount);
        // 탈퇴 여부 확인
        if (!member.isEnabled())
            throw new CustomException(ErrorCode.AccountDisabled);
        member.setLast_login_dt(LocalDateTime.now());

        return member;
    }

    /**
     * 회원가입정보로 임시 계정 상태로 회원을 저장한다.
     * @param addMemberReq 회원가입 정보
     * @return 회원가입 정보
     * @exception CustomException {@link ErrorCode} DuplicatedId 중복 아이디
     */
    
    @Transactional
    public Member signup(MemberDto.AddMemberReq addMemberReq) {
        // 중복 아이디 확인
        if (this.findDuplicateId(addMemberReq.getEmail())){
            throw new CustomException(ErrorCode.DuplicatedId);
        }
        Member member = Member.builder().email(addMemberReq.getEmail())
                .pwd(bCryptPasswordEncoder.encode(addMemberReq.getPwd()))
                .nickname(addMemberReq.getNickname())
                .last_login_dt(LocalDateTime.now())
                .roles(Role.USER.getValue())
                // 이메일 확인 전까지 임시 계정 상태로 생성
                .status(Status.TEMPORARY.ordinal())
                .build();
        return memberRepository.save(member);
    }

    /**
     * 로그인 토큰을 삭제하여 로그아웃을 처리한다.
     * @param member_id 회원 아이디
     * @return 로그아웃한 회원 아이디
     */
    
    public String signout(String member_id) {
        // 로그인 토큰 삭제
        tokenService.findTokenIdAndDeleteToken(member_id, TokenType.login);
        return member_id;
    }

    /**
     * 회원 탈퇴 처리
     * @param member_id 회원 아이디
     * @return 탈퇴된 회원 아이디
     */
    
    @Transactional
    public String withdraw(String member_id) {
        Member member = findByEmail(member_id);
        member.memberWithdraw();
        return member.getEmail();
    }
}
