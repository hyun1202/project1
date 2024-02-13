package com.hyun.jobty.blog.service;

import com.hyun.jobty.blog.domain.Comment;
import com.hyun.jobty.blog.domain.Post;
import com.hyun.jobty.blog.dto.AddCommentReq;
import com.hyun.jobty.blog.dto.AddPostReq;

public interface BlogService {
    Post findByPost(String domain, int post_seq);
    Post savePost(String domain, int menu_id, AddPostReq req);
    Comment saveComment(int post_seq, int member_seq, AddCommentReq req);
}
