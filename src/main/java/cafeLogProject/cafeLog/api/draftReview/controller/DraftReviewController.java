package cafeLogProject.cafeLog.api.draftReview.controller;

import cafeLogProject.cafeLog.api.draftReview.dto.*;
import cafeLogProject.cafeLog.api.draftReview.service.DraftReviewService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.draftReview.DraftReviewNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews/draft")
@RequiredArgsConstructor
public class DraftReviewController {
    private final DraftReviewService draftReviewService;

    @GetMapping("/{draftReviewId}")
    public ResponseEntity<ShowDraftReviewResponse> findByDraftReviewId(@PathVariable(value="draftReviewId") Long draftReviewId,
                                                                       @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        ShowDraftReviewResponse res = draftReviewService.findDraftReview(oAuth2User.getName(), draftReviewId);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShowUserDraftReviewResponse>> findAllReviewsByUser(@Param(value="cafeId") Long cafeId,
                                                                                  @AuthenticationPrincipal CustomOAuth2User oAuth2User){
        List<ShowUserDraftReviewResponse> res;
        if (cafeId == null) {
            res = draftReviewService.findAllReviewsByUser(oAuth2User.getName());
        } else {
            res = draftReviewService.findAllReviewsByUserAndCafe(oAuth2User.getName(), cafeId);
        }
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("")
    public ResponseEntity<ShowDraftReviewResponse> registDraftReview(@RequestBody @Valid RegistDraftReviewRequest registDraftReviewRequest,
                                                                     @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        ShowDraftReviewResponse res = draftReviewService.addDraftReview(oAuth2User.getName(), registDraftReviewRequest);
        return ResponseEntity.ok().body(res);
    }

    @PatchMapping("/{draftReviewId}")
    public ResponseEntity<ShowDraftReviewResponse> updateDraftReview(@PathVariable(value="draftReviewId") Long draftReviewId,
                                                           @RequestBody @Valid UpdateDraftReviewRequest updateDraftReviewRequest,
                                                           @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        ShowDraftReviewResponse res = draftReviewService.updateDraftReview(oAuth2User.getName(), draftReviewId, updateDraftReviewRequest);
        return ResponseEntity.ok().body(res);
    }

//    @DeleteMapping("/{draftReviewId}")
//    public ResponseEntity<?> deleteDraftReview(@PathVariable(value="draftReviewId") Long draftReviewId,
//                                          @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
//        draftReviewService.deleteDraftReviewAndImage(oAuth2User.getName(), draftReviewId);
//        return ResponseEntity.ok().body(null);
//    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteDraftReview(@RequestBody DeleteDraftReviewRequest req,
                                               @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        if (req.getDraftReviewIds() == null || req.getDraftReviewIds().isEmpty()) {
            throw new DraftReviewNotFoundException(ErrorCode.DRAFT_REVIEW_NOT_FOUND_ERROR);
        }
        if (req.getDraftReviewIds().size() == 1) {
            draftReviewService.deleteDraftReviewAndImage(oAuth2User.getName(), req.getDraftReviewIds().get(0));
        } else {
            draftReviewService.deleteDraftReviewsAndImages(oAuth2User.getName(), req.getDraftReviewIds());
        }
        return ResponseEntity.ok().body(null);
    }
}
