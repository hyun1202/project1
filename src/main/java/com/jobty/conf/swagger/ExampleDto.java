package com.jobty.conf.swagger;

import com.jobty.global.response.CommonReason;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExampleDto {
    private List<CommonReason> commonReasons;
}
