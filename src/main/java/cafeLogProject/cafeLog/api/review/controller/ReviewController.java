package cafeLogProject.cafeLog.api.review.controller;

import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.api.review.dto.TagCategory;
import cafeLogProject.cafeLog.api.review.dto.UpdateReviewRequest;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findReview(@PathVariable(value="reviewId") Long reviewId) {
        ShowReviewResponse showReviewResponse = new ShowReviewResponse(reviewService.findReviewById(reviewId));
        return ResponseEntity.ok().body(showReviewResponse);
    }

    // sort 종류 : "NEW", "HIGH_RATING"
    // 최근순 완료 / 별점 높은 순, 낮은 순 개발 필요
    @GetMapping("/list")
    public ResponseEntity<?> showReviews(@RequestParam(required = false, defaultValue = "NEW", value="sort") String sortMethod,
                                         @RequestParam(required = false, defaultValue = "10", value="limit") Integer limit,
                                         @RequestParam(required = false, defaultValue = "3000-01-01T00:00:00", value="timestamp") LocalDateTime timestamp,
                                         @RequestParam(required = false, value="tags") List<Integer> tags,
                                         @RequestParam(required = false, value="rating") Integer rating){
        List<ShowReviewResponse> reviews = reviewService.findReviews(sortMethod, limit, timestamp, tags, rating);
        return ResponseEntity.ok().body(reviews);
    }



    @PostMapping("/")
    public ResponseEntity<?> registReview(@RequestBody @Valid RegistReviewRequest registReviewRequest,
                                          @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        reviewService.addReview(oAuth2User.getName(), registReviewRequest);
        return ResponseEntity.ok().body(null);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable(value="reviewId") Long reviewId,
                                          @RequestBody @Valid UpdateReviewRequest updateReviewRequest,
                                          @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        reviewService.updateReview(oAuth2User.getName(), reviewId, updateReviewRequest);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable(value="reviewId") Long reviewId,
                                          @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        reviewService.deleteReview(oAuth2User.getName(), reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/test")
    public ResponseEntity<?> showReview() {
        return ResponseEntity.ok().body(reviewService.findAllReviews());
    }

}
