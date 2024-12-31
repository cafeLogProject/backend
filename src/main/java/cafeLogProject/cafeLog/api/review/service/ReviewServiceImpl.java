package cafeLogProject.cafeLog.api.review.service;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.common.exception.review.ReviewNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.ReviewSaveException;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final ReviewRepository reviewRepository;
    @Override
    //카페 저장하는 로직 추가 필요
    public Review addReview(long userId, RegistReviewRequest registReviewRequest) {
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
            return reviewRepository.save(registReviewRequest.toEntity(user, cafe));
        } catch (Exception e) {
            throw new ReviewSaveException(ErrorCode.REVIEW_SAVE_ERROR);
        }
    }

    @Override
    public Review findReviewById(long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isEmpty()) throw new ReviewNotFoundException(Long.toString(reviewId), ErrorCode.REVIEW_NOT_FOUND_ERROR);
        return reviewOptional.get();
    }


}
