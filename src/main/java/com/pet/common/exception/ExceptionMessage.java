package com.pet.common.exception;

import com.pet.common.exception.httpexception.BadRequestException;
import com.pet.common.exception.httpexception.InternalServerException;
import com.pet.common.exception.httpexception.NotFoundException;
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
    NOT_FOUND_ACCOUNT(new NotFoundException("해당하는 유저를 찾을 수 없습니다.")),
    DUPLICATION_EMAIL(new BadRequestException("이메일이 중복됩니다.")),
    INVALID_SIGN_UP(new BadRequestException("잘못된 회원가입 입력값입니다.")),
    INVALID_MAIL_KEY(new BadRequestException("잘못된 이메일 인증번호입니다.")),
    FAIL_TO_EMAIL(new BadRequestException("이메일 전송에 실패했습니다.")),

    // 동물
    NOT_FOUND_ANIMAL(new NotFoundException("해당하는 동물 종류를 찾을 수 없습니다.")),
    NOT_FOUND_ANIMAL_KIND(new NotFoundException("해당하는 품종 종류를 찾을 수 없습니다.")),

    // 지역
    NOT_FOUND_CITY(new NotFoundException("해당하는 시도 지역을 찾을 수 없습니다.")),
    NOT_FOUND_TOWN(new NotFoundException("해당하는 시군구 지역을 찾을 수 없습니다.")),


    // 인증
    SHOULD_LOGIN(new AuthenticationException("로그인이 필요합니다.")),
    INVALID_LOGIN(new AuthenticationException("로그인에 실패했습니다.")),
    INVALID_JWT(new AuthenticationException("유효하지 않은 토큰입니다.")),
    INVALID_JWT_EXPIRY(new AuthenticationException("토큰이 만료되었습니다.")),
    NOT_FOUND_GROUP(new AuthenticationException("존재하지 않는 그룹입니다.")),
    NOT_FOUND_PROVIDER(new AuthenticationException("지원하지 않는 인증 방식입니다.")),
    // 댓글

    // 이미지
    FAIL_CHANGE_IMAGE(new IllegalArgumentException("이미지 변환이 실패했습니다.")),
    INVALID_IMAGE_TYPE(new IllegalArgumentException("잘못된 형식의 타입입니다.")),

    // 실종 게시물
    INVALID_MISSING_POST_WRITER(new BadRequestException("현재 유저가 작성한 게시글이 아닙니다.")),

    // 보호소 게시물
    NOT_FOUND_SHELTER_POST(new NotFoundException("해당하는 보호소 게시글을 찾을 수 없습니다."));

    // 알림

    // 태그
    private final RuntimeException exception;

}
