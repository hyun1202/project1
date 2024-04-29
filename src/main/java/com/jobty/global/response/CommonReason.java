package com.jobty.global.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonReason {
    int code;
    String name;
    String msg;
    String remark;
}
