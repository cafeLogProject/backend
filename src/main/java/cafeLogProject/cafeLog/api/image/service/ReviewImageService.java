package cafeLogProject.cafeLog.api.image.service;

import cafeLogProject.cafeLog.api.image.dto.RegistReviewImageResponse;
import cafeLogProject.cafeLog.common.auth.exception.UserNotAuthenticatedException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.image.ImageNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.ReviewNotFoundException;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.image.repository.ReviewImageRepository;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewImageService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Transactional
    public RegistReviewImageResponse addReviewImage(String username, Long reviewId, MultipartFile multipartFile) {
        Review review = validateIdentity(username, reviewId);
        UUID newImageId = UUID.randomUUID();
        ImageUtil.saveImage(ImageUtil.REVIEW_IMAGE_PATH, newImageId.toString(), multipartFile);
        reviewImageRepository.save(ReviewImage.builder()
                        .review(review)
                        .id(newImageId)
                        .build());
        return RegistReviewImageResponse.builder()
                .imageId(newImageId.toString())
                .build();
    }

    public Resource loadReviewImage(String imageId) {
        Resource imageFile = ImageUtil.loadCompressedImage(ImageUtil.REVIEW_IMAGE_PATH, imageId);
        return imageFile;
    }

    @Transactional
    public void deleteReviewImage(String username, String imageId) {
        ReviewImage reviewImage = findByReviewImageIdStr(imageId);
        validateIdentity(username, reviewImage.getReview().getId());

        ImageUtil.deleteCompressedImage(ImageUtil.REVIEW_IMAGE_PATH, imageId);
        if (reviewImage != null) reviewImageRepository.delete(reviewImage);
    }

    private ReviewImage findByReviewImageIdStr(String reviewImageIdStr) {
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

    // 본인의 리뷰인지 검사 & review 리턴
    private Review validateIdentity(String username, Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new ReviewNotFoundException(reviewId.toString(), ErrorCode.REVIEW_NOT_FOUND_ERROR);
        });
        // 해당 리뷰가 본인의 리뷰가 맞는지 확인
        if (!review.getUser().getUsername().equals(username)) {
            throw new UserNotAuthenticatedException(ErrorCode.USER_NOT_AUTH_ERROR);
        }
        return review;
    }
}
