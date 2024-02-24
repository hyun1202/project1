package com.hyun.jobty.domain.blog.dto;

import com.hyun.jobty.domain.blog.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentRes{
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
    List<CommentRes> reply;
    @Builder
    public CommentRes(Comment comment){
        this.comment_id = comment.getSeq();
        this.id = comment.getMember().getId();
        this.nickname = comment.getMember().getNickname();
        this.content = comment.getContent();
        this.is_private = comment.getPrivateYn();
        this.upper_comment_no = comment.getUpperNo();
        this.group_depth = comment.getDepth();
        this.group_ord = comment.getOrd();
        this.reply = comment.getReply() != null? comment.getReply().stream().map(CommentRes::new).collect(Collectors.toList()) : null;
    }
}