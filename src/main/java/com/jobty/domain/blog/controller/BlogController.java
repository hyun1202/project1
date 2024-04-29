package com.jobty.domain.blog.controller;

import com.jobty.domain.blog.domain.Post;
import com.jobty.domain.blog.dto.CommentDto;
import com.jobty.domain.blog.dto.LikeDto;
import com.jobty.domain.blog.dto.PostDto;
import com.jobty.domain.blog.service.BlogService;
import com.jobty.domain.member.service.MemberService;
import com.jobty.global.page.PageDto;
import com.jobty.global.response.ListResult;
import com.jobty.global.response.ResponseService;
import com.jobty.global.response.SingleResult;
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
@RequestMapping("/blog/")
@RestController
// 접속 시 해당하는 스킨(html, css, js)을 불러와야함?
public class BlogController {
    private final MemberService memberService;
    private final BlogService blogService;
    private final ResponseService responseService;

    @Operation(summary = "게시글 목록 조회", description = "도메인, 메뉴에 해당하는 메뉴 조회")
    @GetMapping(value = "{domain}/list/{menu_id}")
    public ResponseEntity<ListResult<PostDto>> getPostList(@PathVariable("domain") String domain,
                                                           @PathVariable("menu_id") Long menu_id,
                                                           PageDto page){
        List<Post> list = blogService.findPostList(PageRequest.of(page.getPage(), page.getSize()), domain, menu_id).getContent();
        List<PostDto> res = list.stream().map(PostDto::new).collect(Collectors.toList());
        return responseService.getListResult(res);
    }

    @Operation(summary = "임시 작성 리스트 조회", description = "임시 작성 리스트 조회")
    @GetMapping(value = "{domain}/tempList/")
    public ResponseEntity<ListResult<PostDto>> getTempPostList(@PathVariable String domain){
        return null;
    }

    @Operation(summary = "게시글 조회", description = "게시글 id에 해당하는 메뉴 조회")
    @GetMapping(value = "/{domain}/{post_id}")
    public ResponseEntity<SingleResult<PostDto.Read>> getPost(@PathVariable("domain") String domain,
                                                         @PathVariable("post_id") Long post_id){
        // 현재 게시글 조회
        Post post = blogService.readPost(domain, post_id);
        PostDto.PrevNextDto prevNextDto = blogService.findPrevNextPost(domain, post.getMenu().getSeq(),post.getSeq());
        // result
        PostDto postDto = PostDto.builder()
                .post(blogService.findPost(domain, post_id))
                .build();
        PostDto.Read res = PostDto.Read.builder()
                .post(postDto)
                .prevNext(prevNextDto)
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "게시글 저장", description = "해당 메뉴에 게시글 저장")
    @PostMapping(value = "/{domain}/{menu_id}")
    public ResponseEntity<SingleResult<PostDto>> addPost(@PathVariable("domain") String domain,
                                                         @PathVariable("menu_id") int menu_id,
                                                         @RequestBody PostDto.AddReq req){

        PostDto res = PostDto.builder()
                .post(blogService.savePost(domain, menu_id, req))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "게시글 수정", description = "해당 게시글 수정")
    @PutMapping(value = "{domain}/{menu_id}/{post_id}")
    public ResponseEntity<SingleResult<PostDto>> updatePost(@PathVariable String domain,
                                                            @PathVariable Long menu_id,
                                                            @PathVariable Long post_id,
                                                            @RequestBody PostDto.AddReq req){
        PostDto res = PostDto.builder()
                .post(blogService.updatePost(domain, menu_id, post_id, req))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "게시글 삭제", description = "해당 게시글 삭제")
    @DeleteMapping(value = "{domain}/{post_id}")
    public ResponseEntity<SingleResult<PostDto>> removePost(@PathVariable String domain,
                                                            @PathVariable Long post_id){
        return null;
    }

    @Operation(summary = "게시글 임시저장", description = "해당 게시글 임시저장")
    @PostMapping(value = "/{domain}/temp/{menu_id}")
    public ResponseEntity<SingleResult<PostDto>> saveTempPost(@PathVariable String domain,
                                                            @PathVariable Long menu_id){
        return null;
    }

    @Operation(summary = "댓글 저장", description = "해당 게시글에 댓글 저장(비회원의 경우 id, pw 입력)")
    @PostMapping(value = "comment/{post_id}")
    public ResponseEntity<SingleResult<CommentDto.Res>> addComment(@PathVariable("post_id") Long post_id,
                                                                   @RequestBody CommentDto.AddReq req){
        // 작성자 작성을 위한 member 조회
        // TODO 비회원인 경우도 댓글을 저장할 수 있음
        // TODO AddReq writer 변경 필요
        String member_uid = memberService.findByEmail(req.getWriter()).getUid();
        CommentDto.Res res = CommentDto.Res.builder()
                .comment(blogService.saveComment(post_id, member_uid, req))
                .build();
        return responseService.getSingleResult(res);
    }

    @Operation(summary = "댓글 수정", description = "해당 댓글 수정")
    @PutMapping(value = "comment/{post_id}")
    public ResponseEntity<SingleResult<CommentDto.Res>> updateComment(@PathVariable String post_id){
        return null;
    }

    @Operation(summary = "댓글 삭제", description = "해당 댓글 삭제")
    @DeleteMapping(value = "comment/{post_id}")
    public ResponseEntity<SingleResult<CommentDto.Res>> removeComment(@PathVariable String post_id){
        return null;
    }

    @Operation(summary = "게시글 좋아요 저장", description = "해당 게시글에 좋아요 설정/삭제")
    @PostMapping(value = "like/{post_id}")
    public ResponseEntity<SingleResult<LikeDto.Res>> postLike(@PathVariable("post_id") Long post_id,
                                                         @RequestBody LikeDto req){
        LikeDto.Res res = LikeDto.Res.builder()
                .status(blogService.postLikeSaveOrDelete(post_id, req))
                .build();
        return responseService.getSingleResult(res);
    }
}
