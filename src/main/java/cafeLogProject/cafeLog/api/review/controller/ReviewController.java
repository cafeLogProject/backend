package cafeLogProject.cafeLog.api.review.controller;

import cafeLogProject.cafeLog.api.review.dto.*;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        List<ShowReviewResponse> res = reviewService.findCafeReviews(cafeId, showCafeReviewRequest);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ShowReviewResponse>> showReviews(@ModelAttribute @Valid ShowReviewRequest showReviewRequest){
        List<ShowReviewResponse> res = reviewService.findReviews(showReviewRequest);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("{draftReviewId}")
    public ResponseEntity<ShowReviewResponse> registReview(@PathVariable(value="draftReviewId") Long draftReviewId,
                                                           @RequestBody @Valid RegistReviewRequest registReviewRequest,
                                                           @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        ShowReviewResponse res = reviewService.addReview(oAuth2User.getName(), draftReviewId, registReviewRequest);
        return ResponseEntity.ok().body(res);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ShowReviewResponse> updateReview(@PathVariable(value="reviewId") Long reviewId,
                                                           @RequestBody @Valid UpdateReviewRequest updateReviewRequest,
                                                           @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        ShowReviewResponse res = reviewService.updateReview(oAuth2User.getName(), reviewId, updateReviewRequest);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable(value="reviewId") Long reviewId,
                                          @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        reviewService.deleteReview(oAuth2User.getName(), reviewId);
        return ResponseEntity.ok().body(null);
    }
}