package com.hyun.jobty.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCommentReq {
    @Schema(description = "유저 ID")
    private String writer;
    @Schema(description = "댓글 내용")
    private String content;
    @Schema(description = "댓글 비공개 여부")
    private boolean is_private;
    @Schema(description = "상위 댓글 번호, 답글이 아닌 경우 사용X")
    @Nullable
    private Long upper_comment_no;
    @Schema(description = "그룹 별 순서")
    private int group_ord;
    @Schema(description = "그룹 내 순서")
    private int group_depth;
}
