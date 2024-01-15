package com.hyun.jobty.member.controller;

import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.global.response.ResponseService;
import com.hyun.jobty.global.response.SingleResult;
import com.hyun.jobty.global.swagger.annotation.ApiErrorCode;
import com.hyun.jobty.member.dto.TokenDto;
import com.hyun.jobty.member.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰 재발급 컨트롤러", description = "토큰 재발급 API")
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
    @Operation(summary = "토큰을 재발급합니다.", description = "토큰값에 해당하는 ID의 토큰을 재발급합니다.")
    @ApiErrorCode(ErrorCode.UnexpectedToken)
    @PostMapping("/")
    public ResponseEntity<SingleResult<TokenDto>> createNewAccessToken(@RequestBody TokenDto request){
        // 토큰에서 유저 아이디 가져옴
        TokenDto newToken = TokenDto.builder()
                .token(tokenService.reissueAccessToken(request.getRefreshToken()))
                .build();
        return responseService.getSingleResult(newToken);
    }
}
