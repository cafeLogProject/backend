package cafeLogProject.cafeLog.api.image.controller;


import cafeLogProject.cafeLog.api.image.dto.RegistReviewImageResponse;
import cafeLogProject.cafeLog.api.image.service.ImageUtil;
import cafeLogProject.cafeLog.api.image.service.ReviewImageService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.image.ImageInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    public ResponseEntity<?> loadReviewImage(@PathVariable(value="imageId") String imageId,
                                             @RequestHeader(value = "If-Modified-Since", required = false) String ifModifiedSince) {
        Resource resource = reviewImageService.loadReviewImage(imageId);
        String contentType = "image/jpeg";

        ZonedDateTime lastModifiedDate = ImageUtil.getLastModifiedDate(resource);
        if (ifModifiedSince != null) {
            try {
                ZonedDateTime ifModifiedSinceDate = ZonedDateTime.parse(ifModifiedSince, DateTimeFormatter.RFC_1123_DATE_TIME);
                if (!lastModifiedDate.isAfter(ifModifiedSinceDate)) {
                    return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();     // 304 리턴
                }
            } catch (Exception e) {
                throw new ImageInvalidException("잘못된 헤더 형식입니다.", ErrorCode.IMAGE_INVALID_ERROR);
            }
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .header(HttpHeaders.LAST_MODIFIED, lastModifiedDate.format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .body(resource);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteReviewImage(@PathVariable(value="imageId") String imageId,
                                               @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        reviewImageService.deleteReviewImage(oAuth2User.getName(), imageId);
        return ResponseEntity.ok().body(null);
    }
}
