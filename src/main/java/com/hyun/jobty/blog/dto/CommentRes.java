package com.hyun.jobty.blog.dto;

import com.hyun.jobty.blog.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentRes{
    String content;
    boolean is_private;
    int upper_no;
    int group_depth;
    int group_ord;
    List<CommentRes> reply;

    @Builder
    public CommentRes(Comment comment){
        this.content = comment.getContent();
        this.is_private = comment.getPrivateYn();
        this.upper_no = comment.getUpperNo();
        this.group_depth = comment.getDepth();
        this.group_ord = comment.getOrd();
        this.reply = comment.getReply() != null? comment.getReply().stream().map(CommentRes::new).collect(Collectors.toList()) : null;
    }
}