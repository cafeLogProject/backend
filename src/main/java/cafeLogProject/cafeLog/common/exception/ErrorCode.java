package cafeLogProject.cafeLog.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    REVIEW_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    REVIEW_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류에 의해 리뷰를 저장하지 못했습니다."),

    CAFE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "카페를 찾을 수 없습니다."),
    CAFE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류에 의해 카페를 저장하지 못했습니다."),

    IMAGE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
    IMAGE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류에 의해 이미지를 저장하지 못했습니다."),
    IMAGE_LOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류에 의해 이미지를 불러오지 못했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
