package cafeLogProject.cafeLog.api.review.service;

import cafeLogProject.cafeLog.api.image.service.ImageService;
import cafeLogProject.cafeLog.api.review.dto.*;
import cafeLogProject.cafeLog.common.auth.exception.UserNotAuthenticatedException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import cafeLogProject.cafeLog.common.exception.cafe.CafeNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.ReviewInvalidSortError;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.common.exception.review.ReviewNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.ReviewSaveException;
import cafeLogProject.cafeLog.domains.review.exception.ReviewDeleteException;
import cafeLogProject.cafeLog.domains.review.exception.ReviewUpdateException;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cafeLogProject.cafeLog.domains.review.domain.Tag.isTagValid;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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

        // 유효한 태그인지 검사
        TagCategory tagCategory = registReviewRequest.getTags();
        tagCategory.isValid();

        // 이미지 관련
        List<String> imageIdsStr = registReviewRequest.getImageIds();
        List<ReviewImage> images = new ArrayList<>();
        for (String imageIdStr : imageIdsStr) {
            // 이미지파일 저장되어있는지 확인, 저장되어있지 않은 경우 실패 답변 전송
            ReviewImage reviewImage = imageService.findReviewImageByReviewImageIdStr(imageIdStr);
            if (reviewImage == null) throw new ReviewNotFoundException(imageIdStr, ErrorCode.IMAGE_NOT_FOUND_ERROR);
        }
        Review newReview;
        try {
            newReview = reviewRepository.save(registReviewRequest.toEntity(user, cafe, images));
        } catch (Exception e) {
            throw new ReviewSaveException(ErrorCode.REVIEW_SAVE_ERROR);
        }
        // 이미지 엔티티에 리뷰 필드 저장
        for (String imageIdStr : imageIdsStr) {
            imageService.addReviewInReviewImage(imageIdStr, newReview.getId());
        }
    }


    @Transactional
    public void updateReview(String username, long reviewId, UpdateReviewRequest updateReviewRequest) {
        Review oldReview = findReviewById(reviewId);
        // 해당 리뷰가 본인의 리뷰가 맞는지 확인
        if (!oldReview.getUser().getUsername().equals(username)) {
            throw new UserNotAuthenticatedException(ErrorCode.USER_NOT_AUTH_ERROR);
        }

        // 유효한 태그인지 검사
        TagCategory tagCategory = updateReviewRequest.getTags();
        tagCategory.isValid();

        // 이미지 관련
        List<String> imageIdsStr = updateReviewRequest.getImageIds();
        if (imageIdsStr == null) {
            // 이미지 변경사항 없는 경우
            try {
                Review updateReview = updateReviewRequest.toEntity(oldReview);
                reviewRepository.save(updateReview);
            } catch (Exception e) {
                throw new ReviewUpdateException(ErrorCode.REVIEW_UPDATE_ERROR);
            }
        } else {
            // 이미지 변경사항 있는 경우
            List<ReviewImage> images = new ArrayList<>();
            for (String imageIdStr : imageIdsStr) {
                // 이미지파일 저장되어있는지 확인, 저장되어있지 않은 경우 실패 답변 전송
                ReviewImage reviewImage = imageService.findReviewImageByReviewImageIdStr(imageIdStr);
                if (reviewImage == null) throw new ReviewNotFoundException(imageIdStr, ErrorCode.IMAGE_NOT_FOUND_ERROR);
                images.add(reviewImage);
            }
            // 새로 저장한 이미지가 있는 경우 그 이미지 엔티티에 리뷰 필드 저장
            for (String imageIdStr : imageIdsStr) {
                imageService.addReviewInReviewImage(imageIdStr, oldReview.getId());
            }
            try {
                Review updatedReview = updateReviewRequest.toEntity(oldReview, images);
                reviewRepository.save(updatedReview);
            } catch (Exception e) {
                throw new ReviewUpdateException(ErrorCode.REVIEW_UPDATE_ERROR);
            }
        }

    }

    @Transactional
    public void deleteReview(String username, long reviewId) {
        Review review = findReviewById(reviewId);
        // 해당 리뷰가 본인의 리뷰가 맞는지 확인
        if (!review.getUser().getId().equals(username)) {
            throw new UserNotAuthenticatedException(ErrorCode.USER_NOT_AUTH_ERROR);
        }

        try{
            // 해당 리뷰의 모든 이미지 삭제
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

    public List<ShowReviewResponse> findReviews(String sortMethod, Integer limit, LocalDateTime timestamp, List<Integer> tags, Integer currentRating) {
        List<Review> reviews = new ArrayList<>();
        if (sortMethod.equals("NEW")) {
            if (tags == null || tags.isEmpty()) {
                reviews = findBeforeDateTime(timestamp);
            } else {
                reviews = findByTagsAndDateTimeOrderByDateTime(tags, timestamp);
            }
        } else if (sortMethod.equals("HIGH_RATING")){
            if (tags == null || tags.isEmpty()) {
                reviews = findBeforeDateTimeOrderByRatingAndDateTime(timestamp, currentRating);
            } else {
                if (currentRating == null) throw new ReviewInvalidSortError("rating 값은 필수입니다.", ErrorCode.REVIEW_INVALID_SORT_ERROR);
                reviews = findByTagsAndDateTimeOrderByRatingAndDateTime(tags, timestamp, currentRating);
            }
        }
        else {
            throw new ReviewInvalidSortError(ErrorCode.REVIEW_INVALID_SORT_ERROR);
        }
        try {
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

    // 테스트용
    public List<ShowReviewResponse> findAllReviews(){
        List<Review> reviews = reviewRepository.findAll();
        List<ShowReviewResponse> showReviewResponses = new ArrayList<>();
        for (Review review : reviews) {
            ShowReviewResponse showReviewResponse = new ShowReviewResponse(review);
            showReviewResponses.add(showReviewResponse);
        }
        return showReviewResponses;
    }

    private List<Review> findByTagsAndDateTimeOrderByRatingAndDateTime(List<Integer> tagIds, LocalDateTime timestamp, int currentRating) {
        try{
            List<Review> reviews = reviewRepository.findByTagsAndDateTimeOrderByRatingAndDateTime(tagIds, timestamp, currentRating);
            return reviews;
        } catch (Exception e) {
            throw new UnexpectedServerException("findByTagsAndDateTimeOrderByRatingAndDateTime 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private List<Review> findByTagsAndDateTimeOrderByDateTime(List<Integer> tagIds, LocalDateTime createdAt) {
        try{
            List<Review> reviews = reviewRepository.findByTagsAndDateTimeOrderByDateTime(tagIds, createdAt);
            return reviews;
        } catch (Exception e) {
            throw new UnexpectedServerException("findByTagsAndDateTimeOrderByDateTime 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private List<Review> findBeforeDateTimeOrderByRatingAndDateTime(LocalDateTime timestamp, int currentRating) {
        try{
            List<Review> reviews = reviewRepository.findBeforeDateTimeOrderByRatingAndDateTime(timestamp, currentRating);
            return reviews;
        } catch (Exception e) {
            throw new UnexpectedServerException("findBeforeDateTimeOrderByRatingAndDateTime 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private List<Review> findBeforeDateTime(LocalDateTime timestamp) {
        try{
            List<Review> reviews = reviewRepository.findBeforeDateTime(timestamp);
            return reviews;
        } catch (Exception e) {
            throw new UnexpectedServerException("findBeforeDateTime 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }



}
