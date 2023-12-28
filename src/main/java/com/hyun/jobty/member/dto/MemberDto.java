package com.hyun.jobty.member.dto;

import com.hyun.jobty.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class MemberDto {

    /**
     * 로그인 요청 데이터
     */
    @Getter
    @NoArgsConstructor
    public static class LoginRequest{

        @NotNull
        private String id;
        @NotNull
        private String pwd;

        public LoginRequest(String id, String pwd){
            this.id = id;
            this.pwd = pwd;
        }
    }

    /**
     * 회원가입 요청 데이터
     */
    @Getter
    public static class AddMemberRequest {
        @NotNull
        @Pattern(regexp = "^[a-z0-9]{4,20}", message = "아이디는 4~20자로 입력해야 합니다.")
        @Schema(description = "아이디", defaultValue = "test1")
        private String id;
        @NotNull
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[!@#$%^&*])[\\da-zA-Z!@#$%^&*]{8,20}", message = "비밀번호는 영문, 숫자, 특수문자 포함 8~20자로 입력해야 합니다.")
        @Schema(description = "비밀번호", defaultValue = "test123!@#")
        private String pwd;
        @Schema(description = "닉네임", defaultValue = "nickname")
        private String nickname;

        public AddMemberRequest(){}
        public AddMemberRequest(String id, String pwd, String nickname){
            this.id = id;
            this.pwd = pwd;
            this.nickname = nickname;
        }

    }

    /**
     * 회원가입 응답 데이터
     */
    @Getter
    public static class Response {
        private String id;
        private String nickname;
        private LocalDateTime last_login_dt;

        @Builder
        public Response(Member member){
            this.id = member.getId();
            this.nickname = member.getNickname();
            this.last_login_dt = member.getLast_login_dt();
        }
    }

    /**
     * 중복 아이디 체크 데이터
     */
    @Getter
    public static class Check{
        private boolean isDuplicate;
        private String msg;

        @Builder
        public Check(boolean isDuplicate, String msg){
            this.isDuplicate = isDuplicate;
            this.msg = msg;
        }
    }
}
