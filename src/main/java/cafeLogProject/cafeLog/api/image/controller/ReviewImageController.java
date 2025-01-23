package cafeLogProject.cafeLog.api.image.controller;


import cafeLogProject.cafeLog.api.image.dto.RegistReviewImageResponse;
import cafeLogProject.cafeLog.api.image.service.ReviewImageService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images/review")
@RequiredArgsConstructor
public class ReviewImageController {
    private final ReviewImageService reviewImageService;

    @PostMapping("{reviewId}")
    public ResponseEntity<RegistReviewImageResponse> registReviewImage(@PathVariable(value="reviewId") Long reviewId,
                                                                       @RequestPart(value="file") MultipartFile image,
                                                                       @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        RegistReviewImageResponse registReviewImageResponse = reviewImageService.addReviewImage(oAuth2User.getName(), reviewId, image);
        return ResponseEntity.ok().body(registReviewImageResponse);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> loadReviewImage(@PathVariable(value="imageId") String imageId) {
        Resource resource = reviewImageService.loadReviewImage(imageId);
        String contentType = "image/jpeg";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteReviewImage(@PathVariable(value="imageId") String imageId,
                                               @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        reviewImageService.deleteReviewImage(oAuth2User.getName(), imageId);
        return ResponseEntity.ok().body(null);
    }
}
