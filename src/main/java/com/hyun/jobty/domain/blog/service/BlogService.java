package com.hyun.jobty.domain.blog.service;

import com.hyun.jobty.domain.blog.domain.Comment;
import com.hyun.jobty.domain.blog.domain.Post;
import com.hyun.jobty.domain.blog.dto.AddCommentReq;
import com.hyun.jobty.domain.blog.dto.AddPostReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    Page<Post> findPostList(Pageable page, String domain, int menu_seq);
    Post findPost(String domain, int post_seq);
    Post savePost(String domain, int menu_id, AddPostReq req);
    Comment saveComment(int post_seq, int member_seq, AddCommentReq req);
    Post findPreviousPost(String domain, int post_seq, int menu_seq);
    Post findNextPost(String domain, int post_seq, int menu_seq);
}
