package com.pet.common.exception;

import com.pet.common.exception.httpexception.BadRequestException;
import com.pet.common.exception.httpexception.InternalServerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    // 서버 관련
    INTERNAL_SERVER(new InternalServerException("서버 에러입니다. 서버 관리자에게 문의주세요.")),

    // 클라이언트
    INVALID_CLIENT_REQUEST(new BadRequestException("잘못된 입력값입니다.")),

    // 회원
    NOT_FOUND_ACCOUNT(new BadRequestException("해당하는 유저를 찾을 수 없습니다.")),

    // 동물

    // 지역

    // 인증
    SHOULD_LOGIN(new AuthenticationException("로그인이 필요합니다.")),
    INVALID_LOGIN(new AuthenticationException("로그인에 실패했습니다.")),
    INVALID_JWT(new AuthenticationException("유효하지 않은 토큰입니다.")),
    INVALID_JWT_EXPIRY(new AuthenticationException("토큰이 만료되었습니다."));
    // 댓글

    // 이미지

    // 실종 게시물

    // 보호소 게시물

    // 알림

    // 태그
    private final RuntimeException exception;

}
