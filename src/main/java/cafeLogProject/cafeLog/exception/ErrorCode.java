package cafeLogProject.cafeLog.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_CREATE_ERROR(HttpStatus.BAD_REQUEST, "사용자 생성에 실패했습니다."),
    USER_EXTRACT_ERROR(HttpStatus.UNAUTHORIZED, "사용자 정보를 추출하는데 실패했습니다."),
    USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_NOT_AUTH_ERROR(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    REVIEW_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),

    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "토큰을 찾지 못했습니다.."),
    TOKEN_INVALID_ERROR(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    TOKEN_NULL_ERROR(HttpStatus.UNAUTHORIZED, "토큰이 비어있습니다."),
    TOKEN_REISSUE_ERROR(HttpStatus.BAD_REQUEST, "토큰 재발급에 실패했습니다."),

    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
