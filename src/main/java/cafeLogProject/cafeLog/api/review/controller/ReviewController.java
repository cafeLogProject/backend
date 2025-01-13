package cafeLogProject.cafeLog.api.review.controller;

import cafeLogProject.cafeLog.api.review.dto.*;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import cafeLogProject.cafeLog.domains.review.domain.Review;
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
    public ResponseEntity<ShowReviewResponse> findReview(@PathVariable(value="reviewId") Long reviewId) {
        ShowReviewResponse showReviewResponse = reviewService.findReview(reviewId);
        return ResponseEntity.ok().body(showReviewResponse);
    }

    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<List<ShowReviewResponse>> showCafeReviews(@PathVariable(value="cafeId") Long cafeId,
                                                                    @ModelAttribute @Valid ShowCafeReviewRequest showCafeReviewRequest) {
        List<ShowReviewResponse> reviews = reviewService.findCafeReviews(cafeId, showCafeReviewRequest);
        return ResponseEntity.ok().body(reviews);
    }

    // sort 종류 : "NEW", "HIGH_RATING"
    // 최근순 완료 / 별점 높은 순, 낮은 순 개발 필요
    @GetMapping("/list")
    public ResponseEntity<List<ShowReviewResponse>> showReviews(@ModelAttribute @Valid ShowReviewRequest showReviewRequest){
        List<ShowReviewResponse> reviews = reviewService.findReviews(showReviewRequest);
        return ResponseEntity.ok().body(reviews);
    }

    @PostMapping("/")
    public ResponseEntity<ShowReviewResponse> registReview(@RequestBody RegistReviewRequest registReviewRequest,
                                          @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        ShowReviewResponse review = reviewService.addReview(oAuth2User.getName(), registReviewRequest);
        return ResponseEntity.ok().body(review);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ShowReviewResponse> updateReview(@PathVariable(value="reviewId") Long reviewId,
                                          @RequestBody @Valid UpdateReviewRequest updateReviewRequest,
                                          @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        ShowReviewResponse review = reviewService.updateReview(oAuth2User.getName(), reviewId, updateReviewRequest);
        return ResponseEntity.ok().body(review);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable(value="reviewId") Long reviewId,
                                          @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        reviewService.deleteReview(oAuth2User.getName(), reviewId);
        return ResponseEntity.ok().body(null);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
