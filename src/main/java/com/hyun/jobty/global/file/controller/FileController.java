package com.hyun.jobty.global.file.controller;

import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.file.FileUtil;
import com.hyun.jobty.global.file.FileVo;
import com.hyun.jobty.global.file.dto.FileDTO;
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
import java.util.stream.Collectors;

@Tag(name = "File API", description = " 파일 변경을 원하면 파일 삭제 후 다시 저장할 것, Last update: 2024.03.27")
@RequiredArgsConstructor
@RestController
public class FileController {
    private final ResponseService responseService;
    private final FileService fileService;
    private FileUtil fileUtil = new FileUtil();

    public enum FileType {
        setting,
        blog
    }

    @Operation(summary = "파일 업로드", description = "이미지, 텍스트 등 파일을 서버에 업로드 합니다.")
    @AccountValidator
    @PostMapping(value = "files/{type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListResult<FileDTO>> uploadFile(@PathVariable FileType type,
                                                          @ModelAttribute FileReq req){
        // path 구조는 enc_id/type/post_seq
        String post_path = "";
        if(req.getPost_id() != null)
            post_path = File.separator + req.strPostId();

        String path = req.EncId() + File.separator + type.name() + post_path;
        // file 저장
        List<FileVo> files = fileService.uploadFiles(path, req);
        return responseService.getListResult(files.stream().map(FileDTO::new).collect(Collectors.toList()));
    }

    @Operation(summary = "파일 다운로드(file_id)", description = "파일 아이디로")
    @GetMapping("files/{file_id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int file_id){
        return responseService.getFileResponseEntity(fileService.getDownloadFilePath(file_id));
    }

    @Operation(summary = "파일 다운로드(path)", description = "파일 다운로드")
    @GetMapping("/files/download/**")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request){
        String path = request.getRequestURI().split(request.getContextPath() + "/files/download")[1];
        return responseService.getFileResponseEntity(new FileVo(path, path, path, ""));
    }

    @Operation(summary = "이미지 파일 출력(img src)", description = "이미지로 저장된 데이터를 html형식으로 출력")
    @GetMapping("/images/**")
    public Resource showImage(HttpServletRequest request) {
        String path = request.getRequestURI().split(request.getContextPath() + "/images")[1];
        return fileService.showImage(path);
    }

    @Operation(summary = "파일 삭제", description = "파일 삭제")
    @AccountValidator
    @DeleteMapping("/files")
    public ResponseEntity<SingleResult<String>> deleteFile(@RequestBody FileDTO fileDTO){
        fileService.deleteFile(fileDTO);
        return responseService.getSingleResult("파일 삭제에 성공했습니다.");
    }
}
