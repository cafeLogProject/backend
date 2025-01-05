package cafeLogProject.cafeLog.api.review.service;

import cafeLogProject.cafeLog.api.image.service.ImageService;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.api.review.dto.TagCategory;
import cafeLogProject.cafeLog.api.review.dto.UpdateReviewRequest;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import cafeLogProject.cafeLog.common.exception.cafe.CafeNotFoundException;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.common.exception.review.ReviewNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.ReviewSaveException;
import cafeLogProject.cafeLog.domains.review.exception.ReviewDeleteException;
import cafeLogProject.cafeLog.domains.review.exception.ReviewInvalidSortError;
import cafeLogProject.cafeLog.domains.review.exception.ReviewUpdateException;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final ReviewRepository reviewRepository;
    private final ImageService imageService;
    @Transactional
    public void addReview (String username, RegistReviewRequest registReviewRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->{
            throw new UserNotFoundException(username, ErrorCode.USER_NOT_FOUND_ERROR);
        });
        Long cafeId = registReviewRequest.getCafeId();
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> {
            throw new CafeNotFoundException(Long.toString(cafeId), ErrorCode.CAFE_NOT_FOUND_ERROR);
        });
        List<String> imageIdsStr = registReviewRequest.getImageIds();
        List<ReviewImage> images = new ArrayList<>();
        for (String imageIdStr : imageIdsStr) {
            // 이미지파일 저장되어있는지 확인, 저장되어있지 않은 경우 실패 답변 전송
            ReviewImage reviewImage = imageService.findReviewImageByReviewImageIdStr(imageIdStr);
            if (reviewImage == null) throw new ReviewNotFoundException(imageIdStr, ErrorCode.IMAGE_NOT_FOUND_ERROR);
        }
        try {
            Review newReview = reviewRepository.save(registReviewRequest.toEntity(user, cafe, images));
            // 이미지 엔티티에 리뷰 필드 저장
            for (String imageIdStr : imageIdsStr) {
                imageService.addReviewInReviewImage(imageIdStr, newReview.getId());
            }
        } catch (Exception e) {
            throw new ReviewSaveException(ErrorCode.REVIEW_SAVE_ERROR);
        }
    }

    // 프론트에서 사진 저장/삭제 요청을 보내고 완료 답변을 받은 뒤에 리뷰 저장/삭제/수정 요청을 보내도록 요구하기
    // 만약 사진 저장/삭제 요청을 보내지 않거나 서버에서 아직 해당 이미지 처리중인데 리뷰 저장/삭제/수정 요청을 보낼 경우 사진 처리가 끝나지 않았다는 에러 답변 보내도록 함.

    @Transactional
    public void updateReview(long reviewId, UpdateReviewRequest updateReviewRequest) {
        Review oldReview = findReviewById(reviewId);

        List<String> imageIdsStr = updateReviewRequest.getImageIds();
        List<ReviewImage> images = new ArrayList<>();
        for (String imageIdStr : imageIdsStr) {
            // 이미지파일 저장되어있는지 확인, 저장되어있지 않은 경우 실패 답변 전송
            ReviewImage reviewImage = imageService.findReviewImageByReviewImageIdStr(imageIdStr);
            if (reviewImage == null) throw new ReviewNotFoundException(imageIdStr, ErrorCode.IMAGE_NOT_FOUND_ERROR);
        }

        try {
            // 새로 저장한 이미지가 있는 경우 그 이미지 엔티티에 리뷰 필드 저장
            for (String imageIdStr : imageIdsStr) {
                imageService.addReviewInReviewImage(imageIdStr, oldReview.getId());
            }

            Review updatedReview = updateReviewRequest.toEntity(oldReview, images);
            reviewRepository.save(updatedReview);
        } catch (Exception e) {
            throw new ReviewUpdateException(ErrorCode.REVIEW_UPDATE_ERROR);
        }
    }

    @Transactional
    public void deleteReview(long reviewId) {
        try{
            // 해당 리뷰의 모든 이미지 삭제
            Review review = findReviewById(reviewId);
            List<ReviewImage> images = review.getImages();
            for (ReviewImage image : images) {
                imageService.deleteReviewImage(image.getId().toString());
            }

            reviewRepository.delete(review);
        } catch (Exception e) {
            throw new ReviewDeleteException(ErrorCode.REVIEW_DELETE_ERROR);
        }
    }

    public Review findReviewById(long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new ReviewNotFoundException(Long.toString(reviewId), ErrorCode.REVIEW_NOT_FOUND_ERROR);
        });
    }

    public List<ShowReviewResponse> findReviews(String sortMethod, Integer limit, LocalDateTime timestamp, TagCategory tags) {
        try {
            List<Review> reviews;
            if (sortMethod == "NEW") {
                reviews = findReviewsByBeforeCreatedAt(timestamp);
            } else {
                throw new ReviewInvalidSortError(ErrorCode.REVIEW_INVALID_SORT_ERROR);
            }
            List<ShowReviewResponse> showReviewResponses = new ArrayList<>();
            for (Review review : reviews) {
                ShowReviewResponse showReviewResponse = new ShowReviewResponse(review);
                showReviewResponses.add(showReviewResponse);
            }
            return showReviewResponses;
        } catch (Exception e) {
            throw new UnexpectedServerException("findReviews 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private List<Review> findReviewsByBeforeCreatedAt(LocalDateTime timestamp) {
        try{
            List<Review> reviews = reviewRepository.findReviewsByBeforeCreatedAt(timestamp);
            return reviews;
        } catch (Exception e) {
            throw new UnexpectedServerException("findReviewsByBeforeCreatedAt 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private List<Review> findByTagIdsContaining(List<Integer> tagIds){
        try{
            List<Review> reviews = reviewRepository.findByTagIdsContaining(tagIds);
            return reviews;
        } catch (Exception e) {
            throw new UnexpectedServerException("findByTagIdsContaining 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }
}
