package com.hyun.jobty.domain.member.service;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.member.domain.Token;
import com.hyun.jobty.domain.member.domain.TokenType;
import com.hyun.jobty.domain.member.dto.MemberDto;
import com.hyun.jobty.domain.member.repository.TokenRepository;
import com.hyun.jobty.util.Util;
import com.hyun.jobty.util.cipher.CipherUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService{
    private final TokenRepository tokenRepository;
    private final JwtTokenService jwtTokenService;

    // 토큰 id 구분자 설정
    private final String token_separator = " ";

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
     * @param email 회원 아이디
     * @param refreshToken 리프레시 토큰
     * @param type 토큰 타입
     * @return 리프레시 토큰 검증 여부
     */
    
    public boolean validRefreshToken(String email, String refreshToken, TokenType type){
        // 토큰 유효성 검사
        if (!jwtTokenService.validJwtToken(refreshToken)){
            return false;
        }
        String token_id = createTokenId(email, TokenType.login);
        String strRefreshToken = findByTokenId(token_id).getRefreshToken();
        if (getTokenType(token_id) == type && strRefreshToken.equals(refreshToken)){
            return true;
        }
        return false;
    }

    /**
     * JWT 토큰 검증
     * @param email 회원 아이디
     * @param accessToken 토큰
     * @return JWT 토큰 검증 여부
     */
    
    public boolean validJwtToken(String email, String accessToken){
        // jwt 토큰 유효성 검사
        if (!jwtTokenService.validJwtToken(accessToken)){
            return false;
        }
        String token_id = createTokenId(email, TokenType.login);
        String strAccessToken = findByTokenId(token_id).getAccessToken();
        // 토큰 타입 및 일치 확인
        if (getTokenType(token_id) == TokenType.login && strAccessToken.equals(accessToken)){
            return true;
        }
        return false;
    }

    /**
     * 해당하는 토큰아이디의 토큰이 토큰 타입과 토큰이 일치한지 확인한다. (토큰검증)
     * @param token_id 토큰 아이디
     * @param accessToken 토큰
     * @param type 토큰 타입
     * @return 토큰 검증 여부
     */
    
    public boolean validToken(String token_id, String accessToken, TokenType type){
        String strAccessToken = findByTokenId(token_id).getAccessToken();
        // 토큰 타입 및 일치 확인
        if (getTokenType(token_id) == type && strAccessToken.equals(accessToken)){
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
     * @param email 토큰에 있는 사용자 아이디
     * @param type 토큰 타입
     * @return 새로 발급된 토큰 정보
     */
    
    public Token createToken(String email, TokenType type){
        // 1. 동일한 토큰이 있는지 확인
        String token_id = createTokenId(email, type);
        if (tokenRepository.findById(token_id).isPresent()){
            // 동일한 토큰이 존재하면 삭제 후 다시 생성한다.
            deleteToken(token_id);
        }

        return tokenRepository.save(
                Token.builder()
                        .id(token_id)
                        .email(email)
                        .accessToken(Util.random(1, 32))
                        .refreshToken("")
                        .exp(type.getExp() * 1000L)
                        .build()
        );
    }

    /**
     * 새로운 jwt 토큰 발급
     * @param dto 회원 정보
     * @return 발급한 토큰 정보
     */
    public Token createJwtToken(MemberDto dto) {
        // 1. 동일한 토큰이 있는지 확인
        String token_id = createTokenId(dto.getEmail(), TokenType.login);
        if (tokenRepository.findById(token_id).isPresent()) {
            // 동일한 토큰이 존재하면 삭제 후 다시 생성한다.
            deleteToken(token_id);
        }
        return tokenRepository.save(
                jwtTokenService.createJwtToken(token_id, dto)
        );
    }

    /**
     * 만료된 토큰 재발급
     * @param refreshToken 리프레시 토큰
     * @param type 토큰 타입
     * @return 새로 생성한 토큰 정보
     * @exception CustomException {@link ErrorCode} UnexpectedToken 토큰 불일치
     */

    public Token reissueAccessToken(String refreshToken, TokenType type){
        String email = jwtTokenService.getTokenEmail(refreshToken);
        // refreshToken 동일한지 확인 후 맞으면 토큰 새로 발급
        if (!validRefreshToken(email, refreshToken, type))
            throw new CustomException(ErrorCode.UnexpectedToken);
        return createToken(email, type);
    }

    /**
     * 토큰 타입 확인
     * @param token_id 토큰 아이디
     * @return 토큰 타입
     */
    private TokenType getTokenType(String token_id){
        // 암호화된 token_id 복호화 후 type 확인
        String type = CipherUtil.decrypt(CipherUtil.NORMAL, token_id).split(token_separator)[1];
        return TokenType.valueOf(type);
    }

    /**
     * 이메일과 토큰 타입을 암호화해 토큰 아이디를 생성한다.
     * @param email 회원 아이디
     * @param type 토큰 타입
     * @return 암호화된 토큰 아이디 문자열
     */
    private String createTokenId(String email, TokenType type){
        // 아이디와 토큰 아이디 구분을 위해 구분자를 넣어서 암호화
        return CipherUtil.encrypt(CipherUtil.NORMAL, email + token_separator + type.name());
    }
}
