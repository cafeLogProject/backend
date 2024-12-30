package cafeLogProject.cafeLog.api.service.Impl;

import cafeLogProject.cafeLog.api.service.ImageService;
import cafeLogProject.cafeLog.api.service.ReviewService;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.domains.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.domains.review.dto.UpdateReviewRequest;
import cafeLogProject.cafeLog.domains.review.exception.*;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.exception.UserNotFoundException;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final ReviewRepository reviewRepository;
    private final ImageService imageService;
    @Override
    //카페 저장하는 로직 추가 필요
    public void addReview(long userId, RegistReviewRequest registReviewRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND_ERROR);
        User user = userOptional.get();

//        if (cafeRepository.existsByCafeNameAndLocationXY()) {
////            //! 카페가 존재하지 않는 경우 카페 저장하는 로직 추가
//
//        }
        Long cafeId = registReviewRequest.getCafeId();
        Optional<Cafe> cafeOptional = cafeRepository.findById(cafeId);
        if (cafeOptional.isEmpty()) {
            throw new ReviewNotFoundException(Long.toString(cafeId), ErrorCode.REVIEW_NOT_FOUND_ERROR);
        };
        Cafe cafe = cafeOptional.get();
        try {
            reviewRepository.save(registReviewRequest.toEntity(user, cafe));
        } catch (Exception e) {
            throw new ReviewSaveException(ErrorCode.REVIEW_SAVE_ERROR);
        }
    }

    @Override
    public void updateReview(long reviewId, UpdateReviewRequest updateReviewRequest) {
        try {
            Review review = findReviewById(reviewId);
            updateReviewRequest.toEntity(review);
            reviewRepository.save(review);
        } catch (Exception e) {
            throw new ReviewUpdateException(ErrorCode.REVIEW_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteReview(long reviewId) {
        try{
            Review review = findReviewById(reviewId);
            List<String> imageIds = review.getImageIds();
            for (String imageId : imageIds) {
                imageService.deleteReviewImage(imageId);
            }
            reviewRepository.delete(review);
        } catch (Exception e) {
            throw new ReviewDeleteException(ErrorCode.REVIEW_DELETE_ERROR);
        }
    }

    @Override
    public Review findReviewById(long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isEmpty()) throw new ReviewNotFoundException(Long.toString(reviewId), ErrorCode.REVIEW_NOT_FOUND_ERROR);
        return reviewOptional.get();
    }

    @Override
    public List<ShowReviewResponse> findReviewListByBeforeCreatedAt(String sortMethod, Integer limit, LocalDateTime timestamp) {
        try {
            Page<Review> reviewPaginated;
            if (sortMethod == "NEW") {
                reviewPaginated = findReviewsByBeforeCreatedAt(limit, timestamp);
            } else {
                throw new ReviewInvalidSortError(ErrorCode.REVIEW_INVALID_SORT_ERROR);
            }
            List<Review> reviewList = reviewPaginated.getContent();
            List<ShowReviewResponse> showReviewResponses = new ArrayList<>();
            for (Review review : reviewList) {
                ShowReviewResponse showReviewResponse = new ShowReviewResponse(review);
                showReviewResponses.add(showReviewResponse);
            }
            return showReviewResponses;
        } catch (Exception e) {
            throw new UnexpectedServerException("findReviewListByBeforeCreatedAt 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private Page<Review> findReviewsByBeforeCreatedAt(Integer pageSize, LocalDateTime timestamp) {
        try{
            Pageable pageable = PageRequest.of(0, pageSize);    //몇번째 페이지, 페이지 당 요소 개수
            Page<Review> paginated = reviewRepository.findReviewsByBeforeCreatedAt(timestamp, pageable);
            if (paginated.isEmpty()) throw new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND_ERROR);
            return paginated;
        } catch (Exception e) {
            throw new UnexpectedServerException("findReviewsByBeforeCreatedAt 에러", ErrorCode.UNEXPECTED_ERROR);
        }
    }
}
