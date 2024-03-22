package com.hyun.jobty.domain.member.dto;

import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.domain.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원가입, 로그인 정보에 관련된 요청, 응답 데이터 형식
 * <pre> 리스트
 *      - 회원가입
 *      - 로그인
 * </pre>
 */
@NoArgsConstructor
@Getter
public class MemberDto {
    @Schema(description = "아이디")
    private String id;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "마지막 로그인 날짜")
    private LocalDateTime last_login_dt;
    @Schema(description = "계정 상태 TEMPORARY: 임시, ACTIVATE: 활성화, DEACTIVATE: 탈퇴")
    private String status;

    @Builder
    public MemberDto(Member member){
        this.id = member.getUsername();
//        this.id = member.getEncUserId();    // 유저아이디 암호화한 값을 return
        this.nickname = member.getNickname();
        this.last_login_dt = member.getLast_login_dt();
        this.status = Status.values()[member.getStatus()].name();
    }
    /**
     * 로그인 요청 데이터
     */
    @Getter
    @NoArgsConstructor
    @Schema(description = "로그인 요청 데이터")
    public static class LoginReq {
        @NotNull
        @Schema(description = "아이디")
        private String id;
        @Schema(description = "비밀번호")
        @NotNull
        private String pwd;

        public LoginReq(String id, String pwd){
            this.id = id;
            this.pwd = pwd;
        }
    }

    /**
     * 회원가입 요청 데이터
     */
    @Getter
    public static class AddMemberReq {
        @NotNull
        @Pattern(regexp = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", message = "아이디는 이메일 형태로 입력되어져야 합니다.")
        @Schema(description = "이메일")
        private String id;
        @NotNull
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[!@#$%^&*])[\\da-zA-Z!@#$%^&*]{8,20}", message = "비밀번호는 영문, 숫자, 특수문자 포함 8~20자로 입력해야 합니다.")
        @Schema(description = "비밀번호(영문,숫자,특수문자 포함 8~20자)")
        private String pwd;
        @Schema(description = "별명")
        private String nickname;

        public AddMemberReq(){}
        public AddMemberReq(String id, String pwd, String nickname){
            this.id = id;
            this.pwd = pwd;
            this.nickname = nickname;
        }

    }

    /**
     * 로그인 응답 데이터
     */
    @Getter
    public static class LoginRes {
        @Schema(description = "사용자 정보")
        private MemberDto member;
        @Schema(description = "토큰 정보")
        private TokenRes token;

        @Builder
        public LoginRes(MemberDto member, TokenRes token){
            this.member = member;
            this.token = token;
        }
    }

    /**
     * 중복 아이디 체크 데이터
     */
    @Getter
    public static class Check{
        @Schema(description = "아이디 중복 여부")
        private boolean duplicate;
        @Schema(description = "중복 확인 메세지", example = "사용 가능한 아이디입니다.")
        private String msg;

        @Builder
        public Check(boolean duplicate, String msg){
            this.duplicate = duplicate;
            this.msg = msg;
        }
    }

    @Getter
    public static class FindReq{
        @Pattern(regexp = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", message = "올바른 이메일 형태가 아니거나 아이디에 허용된 특수문자(-_.) 외 문자열이 입력되었습니다.")
        @Schema(description = "이메일")
        @NotNull
        private String id;
        public FindReq() {}
        public FindReq(String id){
            this.id = id;
        }

    }

    @Getter
    public static class FindRes{
        private String id;
        private String msg;

        public FindRes(){}

        @Builder
        public FindRes(String id, String msg){
            this.id = id;
            this.msg = msg;
        }
    }

    @Getter
    public static class Change{
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[!@#$%^&*])[\\da-zA-Z!@#$%^&*]{8,20}", message = "비밀번호는 영문, 숫자, 특수문자 포함 8~20자로 입력해야 합니다.")
        @Schema(description = "비밀번호(영문,숫자,특수문자 포함 8~20자)")
        @NotNull
        private String pwd;
        public Change(){}
    }
}
