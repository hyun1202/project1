package com.hyun.jobty.domain.blog.service;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.blog.domain.Comment;
import com.hyun.jobty.domain.blog.domain.Like;
import com.hyun.jobty.domain.blog.domain.Post;
import com.hyun.jobty.domain.blog.domain.View;
import com.hyun.jobty.domain.blog.dto.CommentDto;
import com.hyun.jobty.domain.blog.dto.LikeDto;
import com.hyun.jobty.domain.blog.dto.PostDto;
import com.hyun.jobty.domain.blog.repository.CommentRepository;
import com.hyun.jobty.domain.blog.repository.LikeRepository;
import com.hyun.jobty.domain.blog.repository.PostRepository;
import com.hyun.jobty.domain.blog.repository.ViewRepository;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.service.MemberService;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import com.hyun.jobty.domain.setting.detail.service.SettingService;
import com.hyun.jobty.domain.setting.menu.domain.Menu;
import com.hyun.jobty.domain.setting.menu.service.MenuService;
import com.hyun.jobty.util.ClientUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class BlogService{
    private final MemberService memberService;
    private final MenuService menuService;
    private final SettingService settingService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ViewRepository viewRepository;
    private final LikeRepository likeRepository;

    public Page<Post> findPostList(Pageable page, String domain, int menu_seq) {
        Page<Post> list = postRepository.findAllBySetting_domainAndMenu_seq(page, domain, menu_seq);
        if (list == null)
            throw new CustomException(ErrorCode.NotFoundPost);
        return list;
    }

    /**
     * 도메인과 게시글 번호에 해당하는 게시글을 가져온다.
     * @param domain 도메인
     * @param post_seq 게시글 번호
     */
    public Post findPost(String domain, Long post_seq) {
        return postRepository.findBySetting_domainAndSeq(domain, post_seq).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
    }

    /**
     * ip를 확인하여 오늘 날짜에 해당하는 ip가 없으면 조회수 증가
     * 도메인과 게시글번호에 해당하는 게시글을 가져오고 조회수와 좋아요 수를 count해 dto로 반환
     * @param domain 도메인
     * @param post_seq 게시글 번호
     */
    public Post readPost(String domain, Long post_seq){
        LocalDate today = LocalDate.now();
        String ip = ClientUtils.getIp();
        // ip를 확인하여 오늘 날짜에 해당하는 ip가 없으면 조회수 증가
        if (!viewRepository.existsByPostSeqAndDateAndIp(post_seq, today, ip)){
            viewRepository.save(
                View.builder()
                    .postSeq(post_seq)
                    .date(today)
                    .ip(ip)
                    .build()
                );
        }
        return findPost(domain, post_seq);
    }

    public Post savePost(String domain, int menu_seq, PostDto.AddReq req) {
        // 도메인 조회
        Setting setting = settingService.findByDomain(domain);
        // 메뉴 조회
        Menu menu = menuService.findMenuByMenuId(menu_seq);
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

    @Transactional
    public Post updatePost(String domain, Long menu_seq, Long post_seq, PostDto.AddReq req){
        // 게시글 조회
        Post updatePost = findPost(domain, post_seq);
        // 게시글 업데이트

        return null;
    }

    public Comment saveComment(int post_seq, String member_uid, CommentDto.AddReq req) {
        Post post = postRepository.findById(post_seq).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
        Comment comment = Comment.builder()
                .post(post)
                .member(
                        Member.builder().uid(member_uid).build()
                )
                .content(req.getContent())
                .isPrivate(req.is_private())
                .upperNo(req.getUpper_comment_no())
                .ord(req.getGroup_ord())
                .depth(req.getGroup_depth())
                .build();
        return commentRepository.save(comment);
    }

    public PostDto.PrevNextDto findPrevNextPost(String domain, int menu_seq, Long post_seq) {
        return new PostDto.PrevNextDto(postRepository.findPrevNextPost(domain, menu_seq, post_seq));
    }

    /**
     * 해당하는 게시물에 좋아요가 되어있으면 삭제 처리, 안되어있으면 좋아요 처리한다.
     * @param post_seq 게시물 번호
     * @param req 요청 데이터(유저 아이디)
     */
    public boolean postLikeSaveOrDelete(Long post_seq, LikeDto req){
        Member member = memberService.findByEmail(req.getUid());
        Like like = Like.builder()
                .postSeq(post_seq)
                .memberUid(member.getUid())
                .build();
        if (!likeRepository.existsByPostSeqAndMemberUid(post_seq, member.getUid())) {
            // 해당 post에 좋아요가 없으면 생성
            likeRepository.save(like);
            return true;
        }
        // 해당 post에 좋아요가 있으면 삭제
        likeRepository.deleteById(like.getLikeIdInstance());
        return false;
    }
}
