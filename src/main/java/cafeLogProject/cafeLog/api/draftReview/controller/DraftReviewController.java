package cafeLogProject.cafeLog.api.draftReview.controller;

import cafeLogProject.cafeLog.api.draftReview.dto.RegistDraftReviewRequest;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowUserDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.UpdateDraftReviewRequest;
import cafeLogProject.cafeLog.api.draftReview.service.DraftReviewService;
import cafeLogProject.cafeLog.api.image.service.DraftReviewImageService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<ShowUserDraftReviewResponse>> findAllReviewsByUser(@AuthenticationPrincipal CustomOAuth2User oAuth2User){
        List<ShowUserDraftReviewResponse> res = draftReviewService.findAllReviewsByUser(oAuth2User.getName());
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

    @DeleteMapping("/{draftReviewId}")
    public ResponseEntity<?> deleteDraftReview(@PathVariable(value="draftReviewId") Long draftReviewId,
                                          @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        draftReviewService.deleteDraftReviewAndImage(oAuth2User.getName(), draftReviewId);
//        draftReviewImageService.deleteAllImageInDraftReview(oAuth2User.getName(), draftReviewId);
        return ResponseEntity.ok().body(null);
    }
}
