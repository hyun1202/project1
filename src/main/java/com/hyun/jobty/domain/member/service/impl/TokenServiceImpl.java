package com.hyun.jobty.domain.member.service.impl;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.conf.security.jwt.TokenProvider;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.domain.Token;
import com.hyun.jobty.domain.member.domain.TokenType;
import com.hyun.jobty.domain.member.repository.TokenRepository;
import com.hyun.jobty.domain.member.service.TokenService;
import com.hyun.jobty.util.Util;
import com.hyun.jobty.util.cipher.CipherUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


//TODO jwt 토큰과 랜덤 토큰이 혼합되어 있으므로 클래스 분리 필요

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    // 토큰 id 구분자 설정
    private final String token_separator = " ";

    /**
     * 토큰 아이디로 토큰 정보를 찾는다.
     * @param token_id 토큰 아이디
     * @return 토큰 정보
     * @exception CustomException {@link ErrorCode} TokenUserNotFound 토큰 정보가 없음
     */
    @Override
    public Token findByTokenId(String token_id) {
        return tokenRepository.findById(token_id)
                .orElseThrow(() -> new CustomException(ErrorCode.TokenUserNotFound));
    }

    /**
     * 토큰 생성 여부 확인을 위해 유저 아이디로 토큰 아이디를 생성하고 토큰 데이터에 해당 토큰 아이디가 있는지 확인한다.
     * @param member_id 유저 아이디
     * @return 토큰 생성 여부
     */
    @Override
    public void createTokenIdAndCheckByTokenId(String member_id, TokenType type) {
        findByTokenId(createTokenId(member_id, type));
    }


    /**
     * 만료된 토큰 재발급
     * @param refreshToken 리프레시 토큰
     * @param type 토큰 타입
     * @return 새로 생성한 토큰 정보
     * @exception CustomException {@link ErrorCode} UnexpectedToken 토큰 불일치
     */
    @Override
    public Token reissueAccessToken(String refreshToken, TokenType type){
        String member_id = tokenProvider.getMemberId(refreshToken);
        // refreshToken 동일한지 확인 후 맞으면 토큰 새로 발급
        if (!validRefreshToken(member_id, refreshToken, type))
            throw new CustomException(ErrorCode.UnexpectedToken);
        return createToken(member_id, type);
    }

    /**
     * 리프레시 토큰 검증
     * @param member_id 회원 아이디
     * @param refreshToken 리프레시 토큰
     * @param type 토큰 타입
     * @return 리프레시 토큰 검증 여부
     */
    @Override
    public boolean validRefreshToken(String member_id, String refreshToken, TokenType type){
        // 토큰 유효성 검사
        if (!tokenProvider.validToken(refreshToken)){
            return false;
        }
        String token_id = createTokenId(member_id, TokenType.login);
        String strRefreshToken = findByTokenId(token_id).getRefreshToken();
        if (getTokenType(token_id) == type && strRefreshToken.equals(refreshToken)){
            return true;
        }
        return false;
    }

    /**
     * JWT 토큰 검증
     * @param member_id 회원 아이디
     * @param accessToken 토큰
     * @return JWT 토큰 검증 여부
     */
    @Override
    public boolean validJwtToken(String member_id, String accessToken){
        // jwt 토큰 유효성 검사
        if (!tokenProvider.validToken(accessToken)){
            return false;
        }
        String token_id = createTokenId(member_id, TokenType.login);
        String strAccessToken = findByTokenId(token_id).getAccessToken();
        // 토큰 타입 및 일치 확인
        if (getTokenType(token_id) == TokenType.login && strAccessToken.equals(accessToken)){
            return true;
        }
        return false;
    }

    /**
     * 토큰 검증
     * @param token_id 토큰 아이디
     * @param accessToken 토큰
     * @param type 토큰 타입
     * @return 토큰 검증 여부
     */
    @Override
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
    @Override
    public void deleteToken(String token_id) {
        tokenRepository.delete(findByTokenId(token_id));
    }

    /**
     * 회원 아이디와 토큰 타입으로 토큰 아이디를 찾고 토큰을 삭제한다.
     * @param member_id 회원 아이디
     * @param type 토큰 타입
     */
    @Override
    public void findTokenIdAndDeleteToken(String member_id, TokenType type){
        deleteToken(createTokenId(member_id, type));
    }

    /**
     * 새로운 토큰 발급
     * 토큰 타입에 리프레시 토큰이 필요하지 않으면 리프레시 토큰은 생성하지 않는다.
     * @param member_id 토큰에 있는 사용자 아이디
     * @param type 토큰 타입
     * @return 새로 발급된 토큰 정보
     */
    @Override
    public Token createToken(String member_id, TokenType type){
        // 1. 동일한 토큰이 있는지 확인
        String token_id = createTokenId(member_id, type);
        if (tokenRepository.findById(token_id).isPresent()){
            // 동일한 토큰이 존재하면 삭제 후 다시 생성한다.
            deleteToken(token_id);
        }
        String accessToken = Util.random(1, 32);
        String refreshToken = "";

        // jwt 토큰인 경우...
        if (type.isRefreshTokenRequired()) {
            accessToken = getAccessToken(member_id);
            refreshToken = getRefreshToken(member_id);
        }
        return tokenRepository.save(
                Token.builder()
                        .id(token_id)
                        .memberId(member_id)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .exp(type.getExp() * 1000L)
                        .build()
        );
    }

    /**
     * 토큰을 생성한다
     * @param member_id 회원 아이디
     * @return 생성한 토큰 문자열
     */
    private String getAccessToken(String member_id){
        Member member = Member.builder().id(member_id).build();
        return tokenProvider.generateToken(member);
    }

    /**
     * 리프레시 토큰을 생성한다
     * @param member_id 회원 아이디
     * @return 생성한 리프레시 토큰 문자열
     */
    private String getRefreshToken(String member_id){
        Member member = Member.builder().id(member_id).build();
        return tokenProvider.generateRefreshToken(member);
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
     * 회원 아이디와 토큰 타입을 암호화해 토큰 아이디를 생성한다.
     * @param member_id 회원 아이디
     * @param type 토큰 타입
     * @return 암호화된 토큰 아이디 문자열
     */
    private String createTokenId(String member_id, TokenType type){
        // 아이디와 토큰 아이디 구분을 위해 구분자를 넣어서 암호화
        return CipherUtil.encrypt(CipherUtil.NORMAL, member_id + token_separator + type.name());
    }
}
