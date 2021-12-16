package com.pet.common.exception;

import com.pet.common.exception.httpexception.AuthenticationException;
import com.pet.common.exception.httpexception.BadRequestException;
import com.pet.common.exception.httpexception.BaseHttpException;
import com.pet.common.exception.httpexception.ConflictException;
import com.pet.common.exception.httpexception.ForbiddenException;
import com.pet.common.exception.httpexception.InternalServerException;
import com.pet.common.exception.httpexception.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    // 인가
    UN_IDENTIFICATION(new ForbiddenException("권한이 없습니다", 403)),

    // 서버 관련
    INTERNAL_SERVER(new InternalServerException("서버 에러입니다. 서버 관리자에게 문의주세요.", 500)),

    // 회원 6xx
    NOT_FOUND_ACCOUNT(new NotFoundException("해당하는 유저를 찾을 수 없습니다.", 601)),
    DUPLICATION_EMAIL(new ConflictException("이메일이 중복됩니다.", 602)),
    INVALID_SIGN_UP(new BadRequestException("잘못된 회원가입 입력값입니다.", 603)),
    INVALID_MAIL_KEY(new BadRequestException("잘못된 이메일 인증번호입니다.", 604)),
    FAIL_TO_EMAIL(new BadRequestException("이메일 전송에 실패했습니다.", 605)),
    INVALID_PASSWORD(new BadRequestException("비밀번호가 일치하지 않습니다.", 606)),
    INVALID_PASSWORD_REGEX(new BadRequestException("비밀번호가 일치하지 않습니다.", 607)),

    // 동물 7xx
    NOT_FOUND_ANIMAL(new NotFoundException("해당하는 동물 종류를 찾을 수 없습니다.", 701)),
    NOT_FOUND_ANIMAL_KIND(new NotFoundException("해당하는 품종 종류를 찾을 수 없습니다.", 702)),

    // 지역 8xx
    NOT_FOUND_CITY(new NotFoundException("해당하는 시도 지역을 찾을 수 없습니다.", 801)),
    NOT_FOUND_TOWN(new NotFoundException("해당하는 시군구 지역을 찾을 수 없습니다.", 802)),
    NOT_FOUND_INTEREST_AREA(new NotFoundException("해당하는 관심 지역을 찾을 수 없습니다.", 803)),
    INVALID_INTEREST_AREA(new NotFoundException("잘못된 관심지역 요청입니다.", 804)),

    // 인증 9xx,
    SHOULD_LOGIN(new AuthenticationException("로그인이 필요합니다.", 901)),
    INVALID_LOGIN(new AuthenticationException("로그인에 실패했습니다.", 902)),
    INVALID_JWT(new AuthenticationException("유효하지 않은 토큰입니다.", 903)),
    INVALID_JWT_EXPIRY(new AuthenticationException("토큰이 만료되었습니다.", 904)),
    NOT_FOUND_GROUP(new AuthenticationException("존재하지 않는 그룹입니다.", 905)),
    NOT_FOUND_PROVIDER(new AuthenticationException("지원하지 않는 인증 방식입니다.", 906)),
    NOT_FOUND_JWT(new AuthenticationException("토큰이 필요합니다.", 907)),

    // 댓글 10xx
    NOT_FOUND_COMMENT(new NotFoundException("해당하는 댓글을 찾을 수 없습니다.", 1001)),

    // 이미지 11xx
    FAIL_CHANGE_IMAGE(new BadRequestException("이미지 변환이 실패했습니다.", 1101)),
    INVALID_IMAGE_TYPE(new BadRequestException("잘못된 형식의 타입입니다.", 1102)),


    // 실종 게시물 12xx
    NOT_FOUND_MISSING_POST(new NotFoundException("해당하는 실종 게시글을 찾을 수 없습니다.", 1201)),

    // 보호소 게시물 13xx
    NOT_FOUND_SHELTER_POST(new NotFoundException("해당하는 보호소 게시글을 찾을 수 없습니다.", 1301)),

    // 알림 14xx
    NOT_FOUND_NOTIFICATION(new NotFoundException("해당하는 알림을 찾을 수 없습니다..", 1401)),

    // 태그 15xx

    // 통계 16xx
    NOT_FOUND_POST_STATISTICS(new NotFoundException("해당하는 게시글 통계 데이터를 찾을 수 없습니다.", 1601));


    private final RuntimeException exception;

    public static int getCode(ExceptionMessage exceptionMessage) {
        BaseHttpException exception = (BaseHttpException) exceptionMessage.getException();
        return exception.getCode();
    }

}
