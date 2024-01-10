package com.hyun.jobty.member.controller;

import com.hyun.jobty.global.response.ListResult;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.member.dto.TokenDto;
import com.hyun.jobty.member.service.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰 발급 컨트롤러", description = "토큰 발급 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/token")
public class TokenApiController {
    private final ResponseService responseService;
    private final TokenService tokenService;

    /**
     * 리프레시 토큰을 보내오면 확인 후 토큰 재발급
     * @param request {@link com.hyun.jobty.member.dto.TokenDto} 리프레시 토큰 정보
     * @return 새로 발급한 토큰 정보
     */
    @PostMapping("/")
    public ResponseEntity<ListResult<TokenDto>> createNewAccessToken(@RequestBody TokenDto request){
        // 토큰에서 유저 아이디 가져옴
        TokenDto newAccessToken = TokenDto.builder()
                .token(tokenService.reissueAccessToken(request.getRefreshToken()))
                .build();
        return responseService.getListResult("token", newAccessToken);
    }
}
