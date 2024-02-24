package com.hyun.jobty.domain.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Member extends Timestamped implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private int seq;
    @Column(name = "email", nullable = false, unique = true)
    private String id;
    @Column(name = "pwd")
    private String pwd;
    private String nickname;
    private String roles;

    private LocalDateTime last_login_dt;
    private LocalDateTime withdraw_dt;
    // 계정 상태 컬럼 추가(0: 임시, 1: 정상, 2: 탈퇴)
    private int status;

    @Builder
    public Member(int seq, String id, String pwd, String nickname, String roles, LocalDateTime last_login_dt, int status){
        this.seq = seq;
        this.id = id;
        this.pwd = pwd;
        this.nickname = nickname;
        this.roles = roles;
        this.last_login_dt = last_login_dt;
        this.status = status;
    }

    public void setWithdraw_dt(LocalDateTime withdraw_dt) {
        this.withdraw_dt = withdraw_dt;
    }

    public void setLast_login_dt(LocalDateTime last_login_dt) {
        this.last_login_dt = last_login_dt;
    }

    public void updatePassword(String pwd){
        this.pwd = pwd;
    }

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for(String role : roles.split(",")){
            System.out.println("authorities(): " + role);
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }


    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    /**
     * 계정 만료 여부 확인
     * @return 계정이 만료가 되었다면 false, 만료가 아니라면 true
     */
    @Override
    public boolean isAccountNonExpired() {
        if (isAfterMonth(6))
            return false;
        return true;
    }

    // 계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 패스워드 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 계정 사용 가능 여부
    @Override
    public boolean isEnabled() {
        if (this.withdraw_dt != null && this.status == Status.DEACTIVATE.ordinal())
            return false;
        return true;
    }

    // 마지막으로 로그인 한 시간이 정해진 시간이 넘었는지 확인
    private boolean isAfterMonth(int month){
        if (this.last_login_dt.isAfter(LocalDateTime.now().plusMonths(month)))
            return true;
        return false;
    }

    public void memberWithdraw(){
        this.withdraw_dt = LocalDateTime.now();
        this.status = Status.DEACTIVATE.ordinal();
    }

    public void memberActivate(){
        this.status = Status.ACTIVATE.ordinal();
    }

    // 임시 계정인지 확인
    public boolean isTemporaryAccount(int status){
        if (status == Status.TEMPORARY.ordinal())
            return true;
        return false;
    }
}
