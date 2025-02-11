package cafeLogProject.cafeLog.api;

import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/test/http/500")
    public ResponseEntity<?> testGet500Error() {
        throw new UnexpectedServerException("에러 테스트입니다", ErrorCode.UNEXPECTED_ERROR);
    }

    @PostMapping("/test/http/500")
    public ResponseEntity<?> testPost500Error() {
        throw new UnexpectedServerException("에러 테스트입니다", ErrorCode.UNEXPECTED_ERROR);
    }

    @GetMapping("/test/http/204")
    public ResponseEntity<?> testGet204() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test/http/204")
    public ResponseEntity<?> testPost204() {
        return ResponseEntity.noContent().build();
    }
}
