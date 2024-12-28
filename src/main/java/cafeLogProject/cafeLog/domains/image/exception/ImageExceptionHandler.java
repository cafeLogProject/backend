package cafeLogProject.cafeLog.domains.image.exception;

import cafeLogProject.cafeLog.common.dto.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
public class ImageExceptionHandler {

    // 이미지 용량 초과 에러
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> MaxUploadSizeExceededException(MaxUploadSizeExceededException e){
        log.error("이미지 크기 용량을 초과하였습니다.");
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ApiErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE,"이미지 크기 용량을 초과하였습니다."));
    }
}
