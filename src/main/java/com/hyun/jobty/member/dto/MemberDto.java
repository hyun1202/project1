package com.hyun.jobty.member.dto;

import com.hyun.jobty.member.domain.Member;
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
    @Schema(description = "아이디", example = "test1")
    private String id;
    @Schema(description = "닉네임", example = "닉네임")
    private String nickname;
    private LocalDateTime last_login_dt;

    @Builder
    public MemberDto(Member member){
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.last_login_dt = member.getLast_login_dt();
    }
    /**
     * 로그인 요청 데이터
     */
    @Getter
    @NoArgsConstructor
    @Schema(description = "로그인 요청 데이터")
    public static class LoginReq {
        @NotNull
        @Schema(description = "아이디", example = "test1")
        private String id;
        @Schema(description = "비밀번호", example = "test1")
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
        @Pattern(regexp = "^[a-z0-9]{4,20}", message = "아이디는 4~20자로 입력해야 합니다.")
        @Schema(description = "아이디(4~20자)")
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
        private TokenDto token;

        @Builder
        public LoginRes(MemberDto member, TokenDto token){
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
}
