package cafeLogProject.cafeLog.api.image.controller;


import cafeLogProject.cafeLog.api.image.dto.RegistProfileImageResponse;
import cafeLogProject.cafeLog.api.image.service.ProfileImageService;
import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images/profile")
@RequiredArgsConstructor
public class ProfileImageController {
    private final ProfileImageService profileImageService;

    @PostMapping("")
    public ResponseEntity<RegistProfileImageResponse> registProfileImage(@RequestPart(value="file") MultipartFile image,
                                                                         @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        RegistProfileImageResponse response = profileImageService.updateProfileImage(oAuth2User.getName(), image);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> loadProfileImage(@PathVariable(value="userId") String imageId) {
        Resource resource = profileImageService.loadProfileImage(imageId);
        String contentType = "image/jpeg";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteProfileImage(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        profileImageService.deleteProfileImage(oAuth2User.getName());
        return ResponseEntity.ok().body(null);
    }
}
