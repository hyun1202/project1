package com.jobty.domain.member.service;

import com.jobty.advice.exception.CustomException;
import com.jobty.advice.exception.ErrorCode;
import com.jobty.domain.member.domain.Token;
import com.jobty.domain.member.domain.TokenType;
import com.jobty.domain.member.dto.TokenDto;
import com.jobty.domain.member.repository.TokenRepository;
import com.jobty.util.Util;
import com.jobty.util.cipher.CipherUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * JWT 토큰 생성 및 일반 토큰 생성
 * JWT토큰 아이디: uid
 * 일반토큰 아이디: email
 */
@RequiredArgsConstructor
@Service
public class TokenService{
    private final TokenRepository tokenRepository;
    private final JwtTokenService jwtTokenService;

    // 토큰 id 구분자 설정
    private final String token_separator = " ";
    private final int KEY = CipherUtil.NORMAL;

    /**
     * 토큰 아이디로 토큰 정보를 찾는다.
     * @param token_id 토큰 아이디
     * @return 토큰 정보
     * @exception CustomException {@link ErrorCode} TokenUserNotFound 토큰 정보가 없음
     */
    public Token findByTokenId(String token_id) {
        return tokenRepository.findById(token_id)
                .orElseThrow(() -> new CustomException(ErrorCode.TokenUserNotFound));
    }

    /**
     * 토큰 생성 여부 확인을 위해 유저 아이디로 토큰 아이디를 생성하고 토큰 데이터에 해당 토큰 아이디가 있는지 확인한다.
     * @param email 유저 아이디
     */
    public void checkByTokenId(String email, TokenType type) {
        findByTokenId(createTokenId(email, type));
    }

    /**
     * 리프레시 토큰 검증
     * @param tokenDto access 토큰,refresh 토큰
     * @param type 토큰 타입
     * @return 리프레시 토큰 검증 여부
     */
    public Token validRefreshToken(TokenDto.Token tokenDto, TokenType type){
        // 토큰 유효성 검사, access 토큰은 토큰이 정상적인지만 확인(만료 여부 확인X)
        if (!(jwtTokenService.validJwtToken(tokenDto.getRefreshToken())
        && jwtTokenService.validPreAccessToken(tokenDto.getAccessToken()))){
            throw new CustomException(ErrorCode.UnexpectedToken);
        }
        // access 토큰은 이미 만료되었으므로 uid를 가져올 수 없음
        String uid = jwtTokenService.getTokenUid(tokenDto.getRefreshToken());
        String token_id = createTokenId(uid, TokenType.signin);
        Token preToken = findByTokenId(token_id);
        // 타입 및 access 토큰과 refresh 토큰이 동일한지 확인
        if (!(getTokenType(token_id) == type
                && preToken.getRefreshToken().equals(tokenDto.getRefreshToken())
                && preToken.getAccessToken().equals(tokenDto.getAccessToken()))){
            throw new CustomException(ErrorCode.TokenUserNotFound);
        }
        return preToken;
    }

    /**
     * JWT 토큰 검증
     * @param uid 회원 uid
     * @param accessToken 토큰
     * @return JWT 토큰 검증 여부
     */
    public boolean validJwtToken(String uid, String accessToken){
        // jwt 토큰 유효성 검사
        if (!jwtTokenService.validJwtToken(accessToken)){
            return false;
        }
        String token_id = createTokenId(uid, TokenType.signin);
        String strAccessToken = findByTokenId(token_id).getAccessToken();
        // 토큰 타입 및 일치 확인
        if (getTokenType(token_id) == TokenType.signin && strAccessToken.equals(accessToken)){
            return true;
        }
        return false;
    }

    /**
     * 해당하는 토큰아이디의 토큰이 토큰 타입과 토큰이 일치한지 확인한다. (토큰검증)
     * @param token_id 토큰 아이디
     * @param token 토큰
     * @param type 토큰 타입
     * @return 토큰 검증 여부
     */
    public boolean validToken(String token_id, String token, TokenType type){
        // 해당하는 토큰이 있는지 확인
        String strAccessToken = findByTokenId(token_id).getAccessToken();
        // 토큰 타입 및 일치 확인
        if (getTokenType(token_id) == type && strAccessToken.equals(token)){
            return true;
        }
        return false;
    }

    /**
     * 토큰아이디에 맞는 토큰 삭제
     * @param token_id 토큰 아이디
     */
    public void deleteToken(String token_id) {
        tokenRepository.delete(findByTokenId(token_id));
    }

    /**
     * 회원 아이디와 토큰 타입으로 토큰 아이디를 찾고 토큰을 삭제한다.
     * @param email 회원 아이디
     * @param type 토큰 타입
     */
    public void findTokenIdAndDeleteToken(String email, TokenType type){
        deleteToken(createTokenId(email, type));
    }

    /**
     * 새로운 토큰 발급
     * jwt 토큰이 아닌 일반 토큰은 email을 토큰의 id로 이용한다.
     * @param email 토큰에 있는 사용자 아이디
     * @param type 토큰 타입
     * @return 새로 발급된 토큰 정보
     */
    public TokenDto createToken(String email, TokenType type){
        // 1. 동일한 토큰이 있는지 확인 (email로 토큰 id 생성)
        String token_id = createTokenId(email, type);
        if (tokenRepository.findById(token_id).isPresent()){
            // 동일한 토큰이 존재하면 삭제 후 다시 생성한다.
            deleteToken(token_id);
        }
        // 토큰 저장
        Token token = tokenRepository.save(
                Token.builder()
                        .id(token_id)
                        .accessToken(Util.random(1, 32))
                        .refreshToken("")
                        .exp(type.getExp())
                        .build()
        );

        return TokenDto.builder()
                .token_id(token_id)
                .email(getIdByTokenId(token_id))
                .accessToken(token.getAccessToken())
                .type(type)
                .build();
    }

    /**
     * 새로운 jwt 토큰 발급
     * jwt 토큰의 토큰 아이디는 회원 uid로 생성한다.
     * @param tokenDto 회원 정보
     * @return 발급한 토큰 정보
     */
    public TokenDto createJwtToken(TokenDto tokenDto) {
        // 회원 uid로 토큰 id 생성
        String token_id = createTokenId(tokenDto.getUid(), TokenType.signin);
        // 1. 동일한 토큰이 있는지 확인
        if (tokenRepository.findById(token_id).isPresent()) {
            // 동일한 토큰이 존재하면 삭제 후 다시 생성한다.
            deleteToken(token_id);
        }

        Token token = tokenRepository.save(
                jwtTokenService.createJwtToken(token_id, tokenDto)
        );

        return TokenDto.builder()
                .token_id(token.getId())
                .uid(token.getUid())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .type(TokenType.signin)
                .build();
    }

    /**
     * 만료된 토큰 재발급
     * @param tokenDto access 토큰 및 refresh 토큰 정보
     * @param type 토큰 타입
     * @return 새로 생성한 토큰 정보
     * @exception CustomException {@link ErrorCode} UnexpectedToken 토큰 불일치
     */
    public TokenDto reissueAccessToken(TokenDto.Token tokenDto, TokenType type){
        // refreshToken 유효성 검사 및 동일 여부 확인 후 맞으면 토큰 새로 발급
        Token token = validRefreshToken(tokenDto, type);
        // 토큰 업데이트
        jwtTokenService.reissueJwtToken(token);
        tokenRepository.save(token);
        return new TokenDto(token);
    }

    /**
     * 토큰 타입 확인
     * @param token_id 토큰 아이디
     * @return 토큰 타입
     */
    private TokenType getTokenType(String token_id){
        // 암호화된 token_id 복호화 후 type 확인
        String type = CipherUtil.decrypt(KEY, token_id).split(token_separator)[1];
        return TokenType.valueOf(type);
    }

    /**
     * 지정한 id와 토큰 타입을 암호화해 토큰 아이디를 생성한다.
     * 생성 규칙: email + 구분자(" ") + type
     * @param id 회원 아이디 or 회원 uid
     * @param type 토큰 타입
     * @return 암호화된 토큰 아이디 문자열
     */
    private String createTokenId(String id, TokenType type){
        // 아이디와 토큰 아이디 구분을 위해 구분자를 넣어서 암호화
        return CipherUtil.encrypt(KEY, id + token_separator + type.name());
    }

    /**
     * 토큰아이디에 있는 id 리턴.
     * @param token_id 토큰 아이디
     * @return 토큰의 id리턴
     */
    public String getIdByTokenId(String token_id){
        return CipherUtil.decrypt(KEY, token_id).split(token_separator)[0];
    }
}
