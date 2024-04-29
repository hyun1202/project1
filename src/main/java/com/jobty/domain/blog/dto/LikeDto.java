package com.jobty.domain.blog.dto;

import com.jobty.global.accountValidator.dto.ValidatorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeDto extends ValidatorDto {
    @Getter
    @AllArgsConstructor @Builder
    public static class Res{
        boolean status;
    }
}
