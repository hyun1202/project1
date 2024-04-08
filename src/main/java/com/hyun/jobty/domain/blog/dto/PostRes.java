package com.hyun.jobty.domain.blog.dto;

import com.hyun.jobty.domain.blog.domain.Post;
import com.hyun.jobty.domain.blog.domain.PrevNextInterface;
import lombok.AllArgsConstructor;
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
    @Builder
    public PostRes(Post post){
        this.post_seq = post.getSeq();
        this.title = post.getTitle();
        this.thumbnail = post.getThumbnail();
        this.content = post.getContent();
        this.comment = post.getComments() != null? post.getComments().stream().map(CommentRes::new).collect(Collectors.toList()) : null;
    }
    @Getter
    public static class PrevNextRes {
        private PrevNext pre;
        private PrevNext next;
        public PrevNextRes(PrevNextInterface entity){
            this.pre = PrevNext.builder()
                    .post_seq(entity.getPrev_seq())
                    .title(entity.getPrev_title())
                    .build();
            this.next = PrevNext.builder()
                    .post_seq(entity.getNext_seq())
                    .title(entity.getNext_title())
                    .build();
        }
    }
    @AllArgsConstructor
    @Builder
    @Getter
    public static class PrevNext {
        private Long post_seq;
        private String title;
    }
}
