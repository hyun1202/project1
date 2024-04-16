package com.hyun.jobty.domain.blog.dto;

import com.hyun.jobty.domain.blog.domain.Post;
import com.hyun.jobty.domain.blog.domain.PrevNextInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostDto {
    private Long post_seq;
    private String title;
    private String thumbnail;
    private String content;
    private List<CommentDto.Res> comment;
    private Long likeCnt;
    private Long viewCnt;
    @Builder
    public PostDto(Post post){
        this.post_seq = post.getSeq();
        this.title = post.getTitle();
        this.thumbnail = post.getThumbnail();
        this.content = post.getContent();
        this.comment = post.getComments() != null? post.getComments().stream().map(CommentDto.Res::new).collect(Collectors.toList()) : null;
        this.likeCnt = post.getLikeCnt();
        this.viewCnt = post.getViewCnt();
    }
    @Getter
    public static class PrevNextDto {
        private PrevNext pre;
        private PrevNext next;
        public PrevNextDto(PrevNextInterface entity){
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

    @Getter
    @Builder @AllArgsConstructor
    public static class Read {
        public PostDto post;
        public PrevNextDto prevNext;
    }

    @Getter
    public static class AddReq {
        private String thumbnail;
        private String title;
        private String content;
        public AddReq(){}
        public AddReq(String thumbnail, String title, String content){
            this.thumbnail = thumbnail;
            this.title = title;
            this.content = content;
        }
    }
}
