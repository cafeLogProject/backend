package cafeLogProject.cafeLog.api.image.controller;

import cafeLogProject.cafeLog.api.image.dto.RegistDraftReviewImageResponse;
import cafeLogProject.cafeLog.api.image.service.DraftReviewImageService;
import cafeLogProject.cafeLog.api.image.service.ImageUtil;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.image.ImageInvalidException;
import cafeLogProject.cafeLog.common.exception.image.ImageLoadException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
                                                  @AuthenticationPrincipal CustomOAuth2User oAuth2User,
                                                  @RequestHeader(value = "If-Modified-Since", required = false) String ifModifiedSince) {
        Resource resource = draftReviewImageService.loadDraftReviewImage(oAuth2User.getName(), draftReviewId, imageId);
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
                .header(HttpHeaders.LAST_MODIFIED, lastModifiedDate.format(DateTimeFormatter.RFC_1123_DATE_TIME))
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
