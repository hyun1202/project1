package com.hyun.jobty.member.dto;

import com.hyun.jobty.member.domain.Member;
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
        private String id;
        @NotNull
        private String pwd;
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
}
