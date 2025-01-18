package cafeLogProject.cafeLog.api.review.service;

import cafeLogProject.cafeLog.api.image.service.ImageService;
import cafeLogProject.cafeLog.api.review.dto.*;
import cafeLogProject.cafeLog.common.auth.exception.UserNotAuthenticatedException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import cafeLogProject.cafeLog.common.exception.cafe.CafeNotFoundException;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.image.repository.ReviewImageRepository;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.common.exception.review.ReviewNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.ReviewSaveException;
import cafeLogProject.cafeLog.domains.review.domain.Tag;
import cafeLogProject.cafeLog.domains.review.exception.ReviewDeleteException;
import cafeLogProject.cafeLog.domains.review.exception.ReviewUpdateException;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.review.repository.TagRepository;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final ReviewRepository reviewRepository;
    private final ImageService imageService;
    private final TagRepository tagRepository;

    @Transactional
    public ShowReviewResponse addReview (String username, RegistReviewRequest registReviewRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->{
            throw new UserNotFoundException(username, ErrorCode.USER_NOT_FOUND_ERROR);
        });
        Long cafeId = registReviewRequest.getCafeId();
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> {
            throw new CafeNotFoundException(Long.toString(cafeId), ErrorCode.CAFE_NOT_FOUND_ERROR);
        });

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
            saveTags(registReviewRequest.getTagIds(), newReview);
        } catch (Exception e) {
            throw new ReviewSaveException(ErrorCode.REVIEW_SAVE_ERROR);
        }
        // 이미지 엔티티에 리뷰 필드 저장
        for (String imageIdStr : imageIdsStr) {
            imageService.addReviewInReviewImage(imageIdStr, newReview.getId());
        }

        return findReview(newReview.getId());
    }

    @Transactional
    public ShowReviewResponse updateReview(String username, long reviewId, UpdateReviewRequest updateReviewRequest) {
        Review oldReview = reviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new ReviewNotFoundException(Long.toString(reviewId), ErrorCode.REVIEW_NOT_FOUND_ERROR);
        });
        // 해당 리뷰가 본인의 리뷰가 맞는지 확인
        if (!oldReview.getUser().getUsername().equals(username)) {
            throw new UserNotAuthenticatedException(ErrorCode.USER_NOT_AUTH_ERROR);
        }

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

            // 태그 변경사항 있는 경우
            List<Integer> updateTags = updateReviewRequest.getTagIds();
            if (updateTags != null) {
                // 태그 엔티티 모두 삭제후 재생성
                deleteAllReviewTags(updatedReview);
                saveTags(updateReviewRequest.getTagIds(), updatedReview);
            }
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

            // 해당 리뷰의 모든 태그 삭제
            List<Tag> tags = tagRepository.findAllByReview(review);
            tagRepository.deleteAll(tags);

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
            return reviewRepository.search(request.getSort(), request.getTagIds(), request.getRating(), request.getTimestamp(), pageable);
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

    private List<Tag> saveTags(List<Integer> tagIds, Review savedReview) {
        List<Tag> tagList = new ArrayList<>();

        for (Integer tagId : tagIds) {
            tagList.add(Tag.builder()
                    .tagId(tagId)
                    .review(savedReview)
                    .build());
        }
        if (tagList.isEmpty()) {
            return new ArrayList<>();
        } else {
            return tagRepository.saveAll(tagList);
        }
    }

    private void deleteAllReviewTags(Review review) {
        List<Tag> tags = tagRepository.findAllByReview(review);
        tagRepository.deleteAll(tags);
    }



}