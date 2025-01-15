package cafeLogProject.cafeLog.api.review.service;

import cafeLogProject.cafeLog.api.image.service.ImageService;
import cafeLogProject.cafeLog.api.review.dto.*;
import cafeLogProject.cafeLog.common.auth.exception.UserNotAuthenticatedException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import cafeLogProject.cafeLog.common.exception.cafe.CafeNotFoundException;
import cafeLogProject.cafeLog.common.exception.image.ImageInvalidException;
import cafeLogProject.cafeLog.common.exception.review.ReviewInvalidSortError;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.image.repository.ReviewImageRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ReviewImageRepository reviewImageRepository;
    
    //유효한 태그인지 검사하는 로직 필요
    @Transactional
    public ShowReviewResponse addReview (String username, RegistReviewRequest registReviewRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->{
            throw new UserNotFoundException(username, ErrorCode.USER_NOT_FOUND_ERROR);
        });
        Long cafeId = registReviewRequest.getCafeId();
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> {
            throw new CafeNotFoundException(Long.toString(cafeId), ErrorCode.CAFE_NOT_FOUND_ERROR);
        });

        // 유효한 태그인지 검사
//        TagCategory tagCategory = registReviewRequest.getTags();
//        tagCategory.isValid();

        // 이미지 관련
        List<String> imageIdsStr = registReviewRequest.getImageIds();
        for (String imageIdStr : imageIdsStr) {
            // 이미지파일 저장되어있는지 확인, 저장되어있지 않은 경우 실패 답변 전송
            ReviewImage reviewImage = imageService.findByReviewImageIdStr(imageIdStr);
            if (reviewImage == null) throw new ReviewNotFoundException(imageIdStr, ErrorCode.IMAGE_NOT_FOUND_ERROR);
        }

        Review newReview;
        try {
            newReview = reviewRepository.save(registReviewRequest.toEntity(user, cafe));
        } catch (Exception e) {
            throw new ReviewSaveException(ErrorCode.REVIEW_SAVE_ERROR);
        }
        // 이미지 엔티티에 리뷰 필드 저장
        for (String imageIdStr : imageIdsStr) {
            imageService.addReviewInReviewImage(imageIdStr, newReview.getId());
        }

        return findReview(newReview.getId());
    }


    // 유효한 태그인지 검사하는 로직 필요
    @Transactional
    public ShowReviewResponse updateReview(String username, long reviewId, UpdateReviewRequest updateReviewRequest) {
        Review oldReview = reviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new ReviewNotFoundException(Long.toString(reviewId), ErrorCode.REVIEW_NOT_FOUND_ERROR);
        });
        // 해당 리뷰가 본인의 리뷰가 맞는지 확인
        if (!oldReview.getUser().getUsername().equals(username)) {
            throw new UserNotAuthenticatedException(ErrorCode.USER_NOT_AUTH_ERROR);
        }

        // 유효한 태그인지 검사
//        TagCategory tagCategory = updateReviewRequest.getTags();
//        tagCategory.isValid();

        // 이미지 변경사항 있는 경우
        List<String> imageIdsStr = updateReviewRequest.getImageIds();
        if (imageIdsStr != null) {
            List<ReviewImage> images = new ArrayList<>();
            for (String imageIdStr : imageIdsStr) {
                // 이미지파일 저장되어있는지 확인, 저장되어있지 않은 경우 실패 답변 전송
                ReviewImage reviewImage = imageService.findByReviewImageIdStr(imageIdStr);
                if (reviewImage == null) throw new ReviewNotFoundException(imageIdStr, ErrorCode.IMAGE_NOT_FOUND_ERROR);
                images.add(reviewImage);
            }
            // 새로 저장한 이미지가 있는 경우 그 이미지 엔티티에 리뷰 필드 저장
            for (String imageIdStr : imageIdsStr) {
                imageService.addReviewInReviewImage(imageIdStr, oldReview.getId());
            }
        }

        Review updatedReview;
        try {
            updatedReview = reviewRepository.save(updateReviewRequest.toEntity(oldReview));
        } catch (Exception e) {
            throw new ReviewUpdateException(ErrorCode.REVIEW_UPDATE_ERROR);
        }

        return findReview(updatedReview.getId());
    }

    @Transactional
    public void deleteReview(String username, long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new ReviewNotFoundException(Long.toString(reviewId), ErrorCode.REVIEW_NOT_FOUND_ERROR);
        });
        // 해당 리뷰가 본인의 리뷰가 맞는지 확인
        if (!review.getUser().getUsername().equals(username)) {
            throw new UserNotAuthenticatedException(ErrorCode.USER_NOT_AUTH_ERROR);
        }

        try{
            // 해당 리뷰의 모든 이미지 삭제
            imageService.deleteAllReviewImageInReview(review);
            log.error("이미지 삭제 완료");

            reviewRepository.delete(review);
            log.error("리뷰 삭제 완료");
        } catch (Exception e) {
            throw new ReviewDeleteException(ErrorCode.REVIEW_DELETE_ERROR);
        }
    }

    public List<ShowReviewResponse> findCafeReviews(Long cafeId, ShowCafeReviewRequest request){
        Pageable pageable = PageRequest.of(0, request.getLimit());

        try {
            return reviewRepository.searchByCafeId(cafeId, request.getTimestamp(), pageable);
        } catch (Exception e) {
            log.error(e.toString());
            throw new UnexpectedServerException("findReviews 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }

    public List<ShowReviewResponse> findReviews(ShowReviewRequest request) {
        Pageable pageable = PageRequest.of(0, request.getLimit());

        try {
            return reviewRepository.search(request.getSort(), request.getTags(), request.getRating(), request.getTimestamp(), pageable);
        } catch (Exception e) {
            log.error(e.toString());
            throw new UnexpectedServerException("findReviews 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }

    public ShowReviewResponse findReview(Long reviewId){
        ShowReviewResponse showReviewResponse = reviewRepository.findShowReviewResponseById(reviewId);
        if (showReviewResponse == null) {
            throw new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND_ERROR);
        }
        return showReviewResponse;
    }


    // 테스트용
    public List<ShowReviewResponse> findAllReviews(){
        List<Review> reviews = reviewRepository.findAll();
        List<ShowReviewResponse> showReviewResponses = new ArrayList<>();
        for (Review review : reviews) {
            ShowReviewResponse showReviewResponse = reviewRepository.findShowReviewResponseById(review.getId());
            showReviewResponses.add(showReviewResponse);
        }
        return showReviewResponses;
    }




}