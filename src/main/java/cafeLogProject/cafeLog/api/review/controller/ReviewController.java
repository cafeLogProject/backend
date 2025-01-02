package cafeLogProject.cafeLog.api.review.controller;

import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.api.review.dto.UpdateReviewRequest;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> showReview(@PathVariable(value="reviewId") Long reviewId) {
        ShowReviewResponse showReviewResponse = new ShowReviewResponse(reviewService.findReviewById(reviewId));
        return ResponseEntity.ok().body(showReviewResponse);
    }

    // sort 종류 : "NEW",
    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<?> showCafeReviews(@PathVariable(value="cafeId") Long cafeId,
                                             @RequestParam(required = false, defaultValue = "NEW", value="sort") String sortMethod,
                                             @RequestParam(required = false, defaultValue = "10", value="limit") Integer limit,
                                             @RequestParam(required = false, defaultValue = "3000-01-01", value="timestamp") LocalDateTime timestamp){
        List<ShowReviewResponse> reviews = reviewService.findReviews(sortMethod, limit, timestamp);
        return ResponseEntity.ok().body(reviews);
    }
//    @GetMapping("/")
//    public ResponseEntity<?> showReviews(@RequestParam(required = false, defaultValue = "NEW", value="sort") String sortMethod,
//                                         @RequestParam(required = false, defaultValue = "10", value="limit") Integer limit,
//                                         @RequestParam(required = false, defaultValue = "3000-01-01", value="timestamp") LocalDateTime timestamp,
//                                         @RequestParam(required = false, defaultValue = "0", value="rating") Integer rating,
//                                         @RequestParam(required = false, defaultValue = "[]", value="tags") List<Integer> tags) {
////        List<ShowReviewResponse> reviews =
////        return ResponseEntity.ok().body(reviews);
//
//    }


    // 권한 검사 필요
    @PostMapping("/")
    public ResponseEntity<?> registReview(@RequestBody @Valid RegistReviewRequest registReviewRequest) {
        long userId = 1;        //임시값
        reviewService.addReview(userId, registReviewRequest);
        return ResponseEntity.ok().body(null);
    }

    // 권한 검사 필요
    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable(value="reviewId") Long reviewId,
                                          @RequestBody UpdateReviewRequest updateReviewRequest) {
        long userId = 1;
        reviewService.updateReview(reviewId, updateReviewRequest);
        return ResponseEntity.ok().body(null);
    }

    // 권한 검사 필요
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable(value="reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
