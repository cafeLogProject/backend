package cafeLogProject.cafeLog.domains.review.controller;

import cafeLogProject.cafeLog.domains.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.domains.review.service.ReviewService;
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
        long userId = 1;        //임시값
        reviewService.addReview(userId, registReviewRequest);
        return ResponseEntity.ok().body(null);
    }
}
