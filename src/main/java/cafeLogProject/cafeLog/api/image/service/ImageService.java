package cafeLogProject.cafeLog.api.image.service;

import cafeLogProject.cafeLog.api.image.dto.RegistReviewImageResponse;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.image.ImageNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.ReviewNotFoundException;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.image.repository.ReviewImageRepository;
import cafeLogProject.cafeLog.domains.image.util.ImageCompressor;
import cafeLogProject.cafeLog.domains.image.util.ImageHandler;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ImageService {
    private final ReviewImageRepository reviewImageRepository;
    private final  ReviewRepository reviewRepository;

    @Value("${path.base}")
    private String basePath;
    private String testImageRelativePath = "/src/main/resources/static/imgs/test/";
    private String reviewImageRelativePath = "/src/main/resources/static/imgs/review/";
    private String profileImageRelativePath = "/src/main/resources/static/imgs/profile/";

    @Transactional
    public RegistReviewImageResponse addReviewImage(MultipartFile multipartFile) {
        UUID newReviewImageId = UUID.randomUUID();
        ReviewImage newReviewImage = new ReviewImage(newReviewImageId);
        String newImageUuidStr = newReviewImage.getId().toString();
        String path = basePath+reviewImageRelativePath;
        File imageFile = addImage(path, newImageUuidStr, multipartFile);
        ImageCompressor.convertToWebpWithLossless(path, newImageUuidStr, imageFile);  //이미지 압축
        reviewImageRepository.save(newReviewImage);
        return RegistReviewImageResponse.builder()
                .imageId(newImageUuidStr)
                .build();
    }

//    @Transactional
//    //profileImage 엔티티 제작하여 수정 필요
//    public String addProfileImage(MultipartFile multipartFile) {
//        String path = basePath+profileImageRelativePath;
//        File imageFile = addImage(path, multipartFile);
//        String imageFileId = ImageHandler.findImageIdByFile(imageFile);
//        File compressedFile = ImageCompressor.convertToWebpWithLossless(path, imageFile);
//        String imageId = ImageHandler.findImageIdByFile(compressedFile);
//        return imageId;
//    }

    public Resource loadReviewImage(String imageId) {
        String path = basePath+reviewImageRelativePath;
        Resource imageFile = loadCompressedImage(path, imageId);
        return imageFile;
    }


//    public Resource loadProfileImage(String imageId) {
//        String path = basePath+profileImageRelativePath;
//        Resource imageFile = loadImage(path, imageId);
//        return imageFile;
//    }

    @Transactional
    public void deleteReviewImage(String imageIdStr) {
        String path = basePath+reviewImageRelativePath;
        deleteCompressedImage(path, imageIdStr);
        ReviewImage reviewImage = findByReviewImageIdStr(imageIdStr);
        if (reviewImage != null) reviewImageRepository.delete(reviewImage);
    }

    // 특정 리뷰의 모든 리뷰 이미지 삭제
    public void deleteAllReviewImageInReview(Review review){
        String path = basePath+reviewImageRelativePath;
        List<ReviewImage> images = reviewImageRepository.findAllByReview(review);
        for (ReviewImage image : images) {
            deleteCompressedImage(path, image.getId().toString());
            if (image != null) reviewImageRepository.delete(image);
        }
    }

//    @Transactional
//    public void deleteProfileImage(String imageId) {
//        String path = basePath+profileImageRelativePath;
//        ImageHandler.delete(path, imageId);
//    }
    
    // 리뷰 등록시 사진 엔티티 리뷰id 필드에 값 추가하는 기능
    @Transactional
    public void addReviewInReviewImage(String imageIdStr, Long reviewId){
        ReviewImage reviewImage = findByReviewImageIdStr(imageIdStr);
        if (reviewImage == null) throw new ReviewNotFoundException(reviewId.toString(), ErrorCode.IMAGE_NOT_FOUND_ERROR);
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new ReviewNotFoundException(reviewId.toString(), ErrorCode.IMAGE_NOT_FOUND_ERROR);
        });
        if (reviewImage.getReview() != null) return;    //이미 저장된 경우 변경하지 않음
        reviewImage.connectReview(review);
    }

    public ReviewImage findByReviewImageIdStr(String reviewImageIdStr) {
        UUID imageUuid;
        try {
            // UUID자료형으로 형변경
            imageUuid = UUID.fromString(reviewImageIdStr);
        } catch (Exception e) {
            // uuid 형식이 아닌경우
            throw new ImageNotFoundException(ErrorCode.IMAGE_NOT_FOUND_ERROR);
        }
        Optional<ReviewImage> reviewImageOptional = reviewImageRepository.findById(imageUuid);
        if (reviewImageOptional.isEmpty()) return null;
        return reviewImageOptional.get();

    }

    // 테스트 확인용
    public List<String> findAllReviewImageId() {
        List<ReviewImage> reviewImages = reviewImageRepository.findAll();
        List<String> reviewImageIds = new ArrayList<>();
        for (ReviewImage reviewImage : reviewImages) {
//            if (reviewImage.getReview() != null){
//                reviewImageIds.add(reviewImage.getReview().getId().toString());
//            }
            reviewImageIds.add(reviewImage.getId().toString());
        }
        return reviewImageIds;
    }

    private File addImage(String basePath, String imageId, MultipartFile file) {
        ImageHandler.isImageFile(file);
        return ImageHandler.save(basePath, imageId, file);
    }

    private Resource loadCompressedImage(String basePath, String imageId) {
        Resource imageFile = ImageHandler.load(basePath, imageId+".webp");
        return imageFile;
    }

    private void deleteCompressedImage(String basePath, String imageId) {
        ImageHandler.delete(basePath, imageId + ".webp");
    }



}
