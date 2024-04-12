package com.hyun.jobty.domain.blog.dto;

import com.hyun.jobty.domain.blog.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {
    @Getter
    @AllArgsConstructor
    public static class AddReq {
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

    @Getter
    public static class Res {
        @Schema(description = "댓글 번호")
        int comment_id;
        @Schema(description = "회원 아이디")
        String id;
        @Schema(description = "회원 닉네임")
        String nickname;
        @Schema(description = "댓글 내용")
        String content;
        @Schema(description = "댓글 비공개 여부")
        boolean is_private;
        @Schema(description = "상위 댓글 번호")
        Long upper_comment_no;
        @Schema(description = "그룹 별 순서")
        int group_depth;
        @Schema(description = "그룹 내 순서")
        int group_ord;
        @Schema(description = "답글")
        List<Res> reply;
        @Builder
        public Res(Comment comment){
            this.comment_id = comment.getSeq();
            this.id = comment.getMember().getId();
            this.nickname = comment.getMember().getNickname();
            this.content = comment.getContent();
            this.is_private = comment.getPrivateYn();
            this.upper_comment_no = comment.getUpperNo();
            this.group_depth = comment.getDepth();
            this.group_ord = comment.getOrd();
            this.reply = comment.getReply() != null? comment.getReply().stream().map(Res::new).collect(Collectors.toList()) : null;
        }
    }
}
