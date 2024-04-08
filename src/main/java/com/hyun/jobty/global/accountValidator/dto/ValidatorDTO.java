package com.hyun.jobty.global.accountValidator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidatorDTO {
    @Nullable
    @Schema(description = "토큰값과 비교할 id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
