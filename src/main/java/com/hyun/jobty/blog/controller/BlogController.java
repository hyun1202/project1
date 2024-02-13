package com.hyun.jobty.blog.controller;

import com.hyun.jobty.blog.domain.Post;
import com.hyun.jobty.blog.dto.AddCommentReq;
import com.hyun.jobty.blog.dto.AddPostReq;
import com.hyun.jobty.blog.dto.CommentRes;
import com.hyun.jobty.blog.dto.PostRes;
import com.hyun.jobty.blog.service.BlogService;
import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "블로그 게시글 컨트롤러", description = "")
@RequiredArgsConstructor
@RequestMapping("/post/")
@RestController
public class BlogController {
    private final MemberService memberService;
    private final BlogService blogService;
    private final ResponseService responseService;

    @Operation(summary = "게시글 조회", description = "게시글 id에 해당하는 메뉴 조회")
    @GetMapping(value = "/{domain}/{post_id}")
    public ResponseEntity<SingleResult<PostRes>> getPost(@PathVariable("domain") String domain,
                                                         @PathVariable("post_id") int post_id){
        // 현재 게시글 조회
        Post post = blogService.findByPost(domain, post_id);
        // 이전 게시글 조회
        // 다음 게시글 조회

        // result
        PostRes res = PostRes.builder()
                .post(blogService.findByPost(domain, post_id))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "게시글 저장", description = "해당 메뉴에 게시글 저장")
    @PostMapping(value = "/{domain}/{menu_id}")
    public ResponseEntity<SingleResult<PostRes>> addPost(@PathVariable("domain") String domain,
                                                         @PathVariable("menu_id") int menu_id,
                                                         @RequestBody AddPostReq req){

        PostRes res = PostRes.builder()
                .post(blogService.savePost(domain, menu_id, req))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "댓글 저장", description = "해당 게시글에 댓글 저장, writer은 유저ID 입력")
    @PostMapping(value = "co/{post_id}")
    @AccountValidator("writer")
    public ResponseEntity<SingleResult<CommentRes>> addComment(@PathVariable("post_id") int post_id,
                                                               @RequestParam String writer,
                                                               @RequestBody AddCommentReq req){
        // 작성자 작성을 위한 member 조회
        int member_seq = memberService.findByMemberId(writer).getSeq();
        CommentRes res = CommentRes.builder()
                .comment(blogService.saveComment(post_id, member_seq, req))
                .build();
        return responseService.getSingleResult(res);
    }
}
