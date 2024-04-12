package com.hyun.jobty.domain.blog.dto;

import com.hyun.jobty.global.accountValidator.dto.ValidatorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeDto extends ValidatorDTO {
    @Getter
    @AllArgsConstructor @Builder
    public static class Res{
        boolean status;
    }
}
