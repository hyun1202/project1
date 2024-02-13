package com.hyun.jobty.blog.service.impl;

import com.hyun.jobty.blog.domain.Comment;
import com.hyun.jobty.blog.dto.AddCommentReq;
import com.hyun.jobty.blog.repository.CommentRepository;
import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.blog.domain.Post;
import com.hyun.jobty.blog.dto.AddPostReq;
import com.hyun.jobty.blog.repository.PostRepository;
import com.hyun.jobty.blog.service.BlogService;
import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.setting.detail.domain.Setting;
import com.hyun.jobty.setting.detail.service.SettingService;
import com.hyun.jobty.setting.menu.domain.Menu;
import com.hyun.jobty.setting.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {
    private final MenuService menuService;
    private final SettingService settingService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public Post findByPost(String domain, int post_seq) {
        return postRepository.findBySeqAndSetting_domain(post_seq, domain).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
    }

    @Override
    public Post savePost(String domain, int menu_id, AddPostReq req) {
        // 도메인 조회
        Setting setting = settingService.findBySetting(domain);
        // 메뉴 조회
        Menu menu = menuService.findBySingleMenu(menu_id);
        // 게시글 저장
        Post post = Post.builder()
                .setting(setting)
                .menu(menu)
                .thumbnail(req.getThumbnail())
                .title(req.getTitle())
                .content(req.getContent())
                .build();
        return postRepository.save(post);
    }

    @Override
    public Comment saveComment(int post_seq, int member_seq, AddCommentReq req) {
        Post post = postRepository.findById(post_seq).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
        Comment comment = Comment.builder()
                .post(post)
                .member(
                        Member.builder().seq(member_seq).build()
                )
                .content(req.getContent())
                .isPrivate(req.is_private())
                .upperNo(req.getUpper_no())
                .ord(req.getOrd())
                .depth(req.getDepth())
                .build();
        return commentRepository.save(comment);
    }


}
