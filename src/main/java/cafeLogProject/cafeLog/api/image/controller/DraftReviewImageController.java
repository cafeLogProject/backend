package cafeLogProject.cafeLog.api.image.controller;

import cafeLogProject.cafeLog.api.image.dto.RegistDraftReviewImageResponse;
import cafeLogProject.cafeLog.api.image.service.DraftReviewImageService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images/draftReview")
@RequiredArgsConstructor
public class DraftReviewImageController {
    private final DraftReviewImageService draftReviewImageService;

    @PostMapping("{draftReviewId}")
    public ResponseEntity<RegistDraftReviewImageResponse> registDraftReviewImage(@PathVariable(value="draftReviewId") Long draftReviewId,
                                                                            @RequestPart(value="file") MultipartFile image,
                                                                            @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        RegistDraftReviewImageResponse res = draftReviewImageService.addDraftReviewImage(oAuth2User.getName(),draftReviewId,image);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("")
    public ResponseEntity<?> loadDraftReviewImage(@RequestParam(value="draftReviewId") Long draftReviewId,
                                                  @RequestParam(value="imageId") String imageId,
                                                  @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        Resource resource = draftReviewImageService.loadDraftReviewImage(oAuth2User.getName(), draftReviewId, imageId);
        String contentType = "image/jpeg";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    // 부득이하게 파라미터 draftReviewId가 없어져야 한다면 @Query 사용하여 쿼리 짜야 함
    @DeleteMapping("")
    public ResponseEntity<?> deleteDraftReviewImage(@RequestParam(value="draftReviewId") Long draftReviewId,
                                                    @RequestParam(value="imageId") String imageId,
                                                    @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        draftReviewImageService.deleteDraftReviewImage(oAuth2User.getName(), draftReviewId, imageId);
        return ResponseEntity.ok().body(null);
    }

}
