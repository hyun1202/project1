package com.hyun.jobty.domain.blog.service;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.blog.domain.Comment;
import com.hyun.jobty.domain.blog.domain.Post;
import com.hyun.jobty.domain.blog.dto.AddCommentReq;
import com.hyun.jobty.domain.blog.dto.AddPostReq;
import com.hyun.jobty.domain.blog.dto.PostRes;
import com.hyun.jobty.domain.blog.repository.CommentRepository;
import com.hyun.jobty.domain.blog.repository.PostRepository;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.domain.setting.detail.service.SettingService;
import com.hyun.jobty.domain.setting.menu.domain.Menu;
import com.hyun.jobty.domain.setting.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogService{
    private final MenuService menuService;
    private final SettingService settingService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Page<Post> findPostList(Pageable page, String domain, int menu_seq) {
        Page<Post> list = postRepository.findAllBySetting_domainAndMenu_seq(page, domain, menu_seq);
        if (list == null)
            throw new CustomException(ErrorCode.NotFoundPost);
        return list;
    }

    public Post findPost(String domain, int post_seq) {
        return postRepository.findBySeqAndSetting_domain(post_seq, domain).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
    }

    public Post readPost(String domain, int post_seq){
        // TODO 읽을 때 게시글 view수 증가, ip를 확인하여 오늘 날짜에 해당하는 ip가 있으면 조회수 미증가
        return findPost(domain, post_seq);
    }

    public Post savePost(String domain, int menu_id, AddPostReq req) {
        // 도메인 조회
        Setting setting = settingService.findByDomain(domain);
        // 메뉴 조회
        Menu menu = menuService.findMenuByMenuId(menu_id);
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


    public Comment saveComment(int post_seq, String member_seq, AddCommentReq req) {
        Post post = postRepository.findById(post_seq).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
        Comment comment = Comment.builder()
                .post(post)
                .member(
                        Member.builder().seq(member_seq).build()
                )
                .content(req.getContent())
                .isPrivate(req.is_private())
                .upperNo(req.getUpper_comment_no())
                .ord(req.getGroup_ord())
                .depth(req.getGroup_depth())
                .build();
        return commentRepository.save(comment);
    }

    public PostRes.PrevNextRes findPrevNextPost(String domain, int menu_seq, int post_seq) {
        return new PostRes.PrevNextRes(postRepository.findPrevNextPost(domain, menu_seq, post_seq));
    }
}
