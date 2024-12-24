package cafeLogProject.cafeLog.controller;


import cafeLogProject.cafeLog.dto.ImageDto;
import cafeLogProject.cafeLog.dto.image.ImageResponseDto;
import cafeLogProject.cafeLog.entity.Cafe;
import cafeLogProject.cafeLog.entity.Review;
import cafeLogProject.cafeLog.service.CafeService;
import cafeLogProject.cafeLog.service.ImageService;
import cafeLogProject.cafeLog.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final ReviewService reviewService;

    @PostMapping("/")
    public ResponseEntity<?> registReviewImage(@RequestPart(value="file") List<ImageDto> imageDtoList,
                                               @RequestParam(value="reviewId") long reviewId) {
        // dto에 필수 입력란 @NotNull 설정 필요, @Valid 이용하여 필수 입력란 누락시 자동으로 400 error 리턴하도록 함
        long userId = 1;
        Review review = reviewService.findReviewById(reviewId);
//        if (cafe == null) return 실패응답
        imageService.addReviewImages(imageDtoList, review);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/test")
    public ResponseEntity<?> saveReviewImageTest(@RequestPart(value="file") MultipartFile file){
//    public ResponseEntity<?> saveImageTest(@RequestPart(value="file") ImageDto imageDto){
        ImageDto imageDto = ImageDto.builder()
                .file(file)
                .build();
        String imageId = imageService.addImage(imageDto);
        return ResponseEntity.ok().body(imageId);
    }


    //오류발생
    @GetMapping("/")
    public ResponseEntity<Resource> getImage(@RequestParam(value="imageIds") String imageId) {
        Resource resource = imageService.loadImage(imageId).getImageFile();
        String contentType = "image/jpeg";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
