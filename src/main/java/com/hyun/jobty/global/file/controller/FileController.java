package com.hyun.jobty.global.file.controller;

import com.hyun.jobty.global.accountValidator.annotation.AccountValidator;
import com.hyun.jobty.global.file.dto.FileDto;
import com.hyun.jobty.global.file.dto.FileReq;
import com.hyun.jobty.global.file.service.FileService;
import com.hyun.jobty.global.response.ListResult;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Tag(name = "File API", description = "type의 setting: 블로그 썸네일 이미지, blog: 블로그 내 첨부 이미지, 파일 변경을 원하면 파일 삭제 후 다시 저장해주세요. Last update: 2024.03.27")
@RequiredArgsConstructor
@RestController
public class FileController {
    private final ResponseService responseService;
    private final FileService fileService;
    public enum FileType {
        setting,
        blog
    }

    @Operation(summary = "파일 업로드", description = "이미지, 텍스트 등 파일을 서버에 업로드 합니다.")
    @AccountValidator
    @PostMapping(value = "files/{type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListResult<FileDto>> uploadFile(@PathVariable FileType type,
                                                          @ModelAttribute FileReq req){
        // path 구조는 enc_id/type/post_seq
        String post_path = "";
        if(req.getPost_id() != null)
            post_path = File.separator + req.strPostId();
        String noUidPath= File.separator + type.name() + post_path + File.separator;
        // 요청시에는 uid를 암호화 하지않고 요청해야함
        String path = req.getUid() + noUidPath;
        // 실제 파일은 uid를 암호화하여 저장
        String savePath = fileService.getEncUid(req.getUid()) + noUidPath;
        // file 저장
        List<FileDto> res = fileService.uploadFiles(path, savePath, req);
        return responseService.getListResult(res);
    }

    @Operation(summary = "파일 다운로드(file_id)", description = "파일 아이디로")
    @GetMapping("files/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId){
        return responseService.getFileResponseEntity(fileService.downloadFile(fileId));
    }

    @Operation(summary = "파일 다운로드(path)", description = "파일 다운로드")
    @GetMapping("/files/download/**")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
        String reqUri = request.getRequestURI().split(request.getContextPath() + "/files/download")[1];
        String uid = reqUri.split("/")[1];
        // 실제 파일 저장은 uid를 암호화해서 저장되므로 uid를 암호화하여 실제 파일 접근
        String uri = reqUri.replace(uid, fileService.getEncUid(uid));
        FileDto res = fileService.downloadFile(uri);
        return responseService.getFileResponseEntity(res);
    }

    @Operation(summary = "이미지 파일 출력(img src)", description = "이미지로 저장된 데이터를 html형식으로 출력")
    @GetMapping("/files/images/**")
    public Resource showImage(HttpServletRequest request) {
        String reqUri = request.getRequestURI().split(request.getContextPath() + "/files/images")[1];
        String uid = reqUri.split("/")[1];
        // 실제 파일 저장은 uid를 암호화해서 저장되므로 uid를 암호화하여 실제 파일 접근
        String uri = reqUri.replace(uid, fileService.getEncUid(uid));
        return fileService.showImage(uri);
    }

    @Operation(summary = "파일 삭제", description = "파일 삭제")
    @AccountValidator
    @DeleteMapping("/files")
    public ResponseEntity<SingleResult<String>> deleteFile(@RequestBody FileDto.Delete fileDTO){
        fileService.deleteFile(fileDTO);
        return responseService.getSingleResult("파일 삭제에 성공했습니다.");
    }
}
