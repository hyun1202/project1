package com.hyun.jobty.global.accountValidator.dto;

import com.hyun.jobty.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidatorDto {
    @Nullable
    @Schema(description = "토큰값과 비교할 uid")
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * uid만 넣은 member entity를 생성 한다.
     * @return member entity
     */
    public Member uidToEntity(){
        return Member.builder()
                .uid(this.uid)
                .build();
    }
}
