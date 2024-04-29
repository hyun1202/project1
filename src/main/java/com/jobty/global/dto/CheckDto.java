package com.jobty.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 중복 체크 데이터
 */
@Getter
public class CheckDto {
    @Schema(description = "중복 여부")
    private boolean duplicate;
    @Schema(description = "중복 확인 메세지", example = "사용 가능한 아이디입니다.")
    private String msg;

    @Builder
    public CheckDto(boolean duplicate, String msg) {
        this.duplicate = duplicate;
        this.msg = msg;
    }
}
