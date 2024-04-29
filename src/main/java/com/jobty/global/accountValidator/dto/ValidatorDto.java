package com.jobty.global.accountValidator.dto;

import com.jobty.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidatorDto {
    @Nullable
    @Schema(description = "토큰값과 비교할 uid")
    private String uid;

    protected ValidatorDto(String uid){
        this.uid = uid;
    }

    public String getUid() {
        return uid;
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
