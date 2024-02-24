package com.hyun.jobty.domain.blog.controller;

import com.hyun.jobty.domain.blog.domain.Post;
import com.hyun.jobty.domain.blog.dto.AddCommentReq;
import com.hyun.jobty.domain.blog.dto.AddPostReq;
import com.hyun.jobty.domain.blog.dto.CommentRes;
import com.hyun.jobty.domain.blog.dto.PostRes;
import com.hyun.jobty.domain.blog.service.BlogService;
import com.hyun.jobty.global.page.PageDto;
import com.hyun.jobty.global.response.ListResult;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "블로그 게시글 컨트롤러", description = "")
@RequiredArgsConstructor
@RequestMapping("/post/")
@RestController
public class BlogController {
    private final MemberService memberService;
    private final BlogService blogService;
    private final ResponseService responseService;

    @Operation(summary = "게시글 목록 조회", description = "도메인, 메뉴에 해당하는 메뉴 조회")
    @GetMapping(value = "list/{domain}/{menu_id}")
    public ResponseEntity<ListResult<PostRes>> getPostList(@PathVariable("domain") String domain,
                                                           @PathVariable("menu_id") int menu_id,
                                                           PageDto page){
        List<Post> list = blogService.findPostList(PageRequest.of(page.getPage(), page.getSize()), domain, menu_id).getContent();
        List<PostRes> res = list.stream().map(PostRes::new).collect(Collectors.toList());
        return responseService.getListResult(res);
    }

    @Operation(summary = "게시글 조회", description = "게시글 id에 해당하는 메뉴 조회")
    @GetMapping(value = "/{domain}/{post_id}")
    public ResponseEntity<SingleResult<PostRes>> getPost(@PathVariable("domain") String domain,
                                                         @PathVariable("post_id") int post_id){
        // 현재 게시글 조회
        Post post = blogService.findPost(domain, post_id);
        // 이전 게시글 조회

        // 다음 게시글 조회

        // result
        PostRes res = PostRes.builder()
                .post(blogService.findPost(domain, post_id))
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
//    @AccountValidator(value = "req", field = "writer", type = ElementType.FIELD)
    public ResponseEntity<SingleResult<CommentRes>> addComment(@PathVariable("post_id") int post_id,
                                                               @RequestBody AddCommentReq req){
        // 작성자 작성을 위한 member 조회
        int member_seq = memberService.findByMemberId(req.getWriter()).getSeq();
        CommentRes res = CommentRes.builder()
                .comment(blogService.saveComment(post_id, member_seq, req))
                .build();
        return responseService.getSingleResult(res);
    }
}
