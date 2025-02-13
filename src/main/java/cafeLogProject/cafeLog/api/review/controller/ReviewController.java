package cafeLogProject.cafeLog.api.review.controller;

import cafeLogProject.cafeLog.api.review.dto.*;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public ResponseEntity<ShowReviewResponse> findReviewByReviewId(@PathVariable(value="reviewId") Long reviewId) {
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

    @GetMapping("/my")
    public ResponseEntity<List<ShowReviewResponse>> showMyReviews(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                                                                  @ModelAttribute @Valid ShowUserReviewRequest showUserReviewRequest){
        List<ShowReviewResponse> review = reviewService.findUserReviews(oAuth2User.getName(), showUserReviewRequest);
        return ResponseEntity.ok().body(review);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ShowReviewResponse>> showReviewsByUserId(@NotNull(message = "userId는 필수입니다") Long userId,
                                                                  @ModelAttribute @Valid ShowUserReviewRequest showUserReviewRequest){
        List<ShowReviewResponse> review = reviewService.findUserReviews(userId, showUserReviewRequest);
        return ResponseEntity.ok().body(review);
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

    @GetMapping("/follow")
    public ResponseEntity<List<ShowReviewResponse>> showFollowingReviews(@ModelAttribute @Valid ShowFollowingReviewRequest showFollowingReviewRequest,
                                                  @AuthenticationPrincipal CustomOAuth2User oAuth2User){
//        List<ShowReviewResponse> res = reviewService.findFollowingReviews(oAuth2User.getName(), showFollowingReviewRequest);
        
        // 프론트 테스트용 mock 제작
        LocalDate visitDate = LocalDate.of(2025,2,1);
        LocalDateTime createdAt = LocalDateTime.now();
        List<Integer> tagIds = Arrays.asList(9999,8888);
        ShowReviewResponse mock = new ShowReviewResponse((long)1000,"내용",5,visitDate, null, new HashSet<>(tagIds), (long)1, "스타벅스", (long)1, "닉네임", false, createdAt);
        List<ShowReviewResponse> mockRes = List.of(mock, mock, mock,mock,mock);
        return ResponseEntity.ok().body(mockRes);
    }
}