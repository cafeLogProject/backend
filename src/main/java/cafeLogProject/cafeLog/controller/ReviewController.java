package cafeLogProject.cafeLog.controller;

import cafeLogProject.cafeLog.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping("/")
    public ResponseEntity<?> registReview(@RequestBody @Valid RegistReviewRequest registReviewRequest) {
        // dto에 필수 입력란 @NotNull 설정 필요, @Valid 이용하여 필수 입력란 누락시 자동으로 400 error 리턴하도록 함
        long userId = 1;
        reviewService.addReview(userId, registReviewRequest);
        return ResponseEntity.ok().body(null);
    }
}
