package cafeLogProject.cafeLog.api.controller;


import cafeLogProject.cafeLog.api.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/")
    public ResponseEntity<?> registReviewImage(@RequestPart(value="file") MultipartFile image) {
        String imageId = imageService.addReviewImage(image);
        return ResponseEntity.ok().body(imageId);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> loadReviewImage(@PathVariable(value="imageId") String imageId) {
        Resource resource = imageService.loadReviewImage(imageId);
        String contentType = "image/jpeg";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteReviewImage(@PathVariable(value="imageId") String imageId) {
        imageService.deleteReviewImage(imageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
