package com.hyun.jobty.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCommentReq {
    private String content;
    private boolean is_private;
    private int upper_no;
    private int ord;
    private int depth;
}
