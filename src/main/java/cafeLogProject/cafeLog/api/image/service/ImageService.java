package cafeLogProject.cafeLog.api.image.service;

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
    public String addReviewImage(MultipartFile multipartFile) {
        UUID newReviewImageId = UUID.randomUUID();
        ReviewImage newReviewImage = new ReviewImage(newReviewImageId);
        String newImageUuidStr = newReviewImage.getId().toString();
        String path = basePath+reviewImageRelativePath;
        File imageFile = addImage(path, newImageUuidStr, multipartFile);
        File compressedFile = ImageCompressor.convertToWebpWithLossless(path, newImageUuidStr, imageFile);  //이미지 압축
        reviewImageRepository.save(newReviewImage);
        return newImageUuidStr;
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
        log.warn("동작1");
        String path = basePath+reviewImageRelativePath;
        deleteCompressedImage(path, imageIdStr);
        log.warn("동작2");
        ReviewImage reviewImage = findReviewImageByReviewImageIdStr(imageIdStr);
        log.warn("동작3");
        if (reviewImage == null) return;
        Review review = reviewImage.getReview();
        if (review == null) return;
        log.warn("동작4");
        review.removeImage(reviewImage);
        log.warn("동작5");
        reviewImageRepository.delete(reviewImage);
        log.warn("동작6");
    }

//    @Transactional
//    public void deleteProfileImage(String imageId) {
//        String path = basePath+profileImageRelativePath;
//        ImageHandler.delete(path, imageId);
//    }
    
    // 리뷰 등록시 사진 엔티티 리뷰id 필드에 값 추가하는 기능
    @Transactional
    public void addReviewInReviewImage(String imageIdStr, Long reviewId){
        ReviewImage reviewImage = findReviewImageByReviewImageIdStr(imageIdStr);
        if (reviewImage == null) throw new ReviewNotFoundException(reviewId.toString(), ErrorCode.IMAGE_NOT_FOUND_ERROR);
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new ReviewNotFoundException(reviewId.toString(), ErrorCode.IMAGE_NOT_FOUND_ERROR);
        });
        if (reviewImage.getReview() != null) return;    //이미 저장된 경우 변경하지 않음
        reviewImage.connectReview(review);
    }

    public ReviewImage findReviewImageByReviewImageIdStr(String reviewImageIdStr) {
        UUID imageUuid = UUID.fromString(reviewImageIdStr);
        Optional<ReviewImage> reviewImageOptional = reviewImageRepository.findById(imageUuid);
        if (reviewImageOptional.isEmpty()) return null;
        return reviewImageOptional.get();
    }

    public List<String> findAllReviewImageId() {
        List<ReviewImage> reviewImages = reviewImageRepository.findAll();
        List<String> reviewImageIds = new ArrayList<>();
        for (ReviewImage reviewImage : reviewImages) {
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
