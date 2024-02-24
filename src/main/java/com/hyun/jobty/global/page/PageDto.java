package com.hyun.jobty.global.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageDto {
    @Schema(description = "페이지 번호")
    int page;
    @Schema(description = "페이지 크기")
    int size;
}
