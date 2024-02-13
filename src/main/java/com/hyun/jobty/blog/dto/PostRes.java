package com.hyun.jobty.blog.dto;

import com.hyun.jobty.blog.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostRes {
    private int post_seq;
    private String title;
    private String thumbnail;
    private String content;
    private List<CommentRes> comment;
    // 다음글
    private PostRes pre;
    // 이전글
    private PostRes next;
    @Builder
    public PostRes(Post post){
        this.post_seq = post.getSeq();
        this.title = post.getTitle();
        this.thumbnail = post.getThumbnail();
        this.content = post.getContent();
        this.comment = post.getComments() != null? post.getComments().stream().map(CommentRes::new).collect(Collectors.toList()) : null;
    }
}
