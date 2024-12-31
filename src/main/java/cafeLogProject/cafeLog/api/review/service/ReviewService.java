package cafeLogProject.cafeLog.api.review.service;

import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cafeLogProject.cafeLog.api.review.dto.UpdateReviewRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public interface ReviewService {

    @Transactional
    void addReview(long userId, RegistReviewRequest registReviewRequest);
    @Transactional
    void updateReview(long reviewId, UpdateReviewRequest updateReviewRequest);
    @Transactional
    void deleteReview(long reviewId);
    Review findReviewById(long reviewId);
    List<ShowReviewResponse> findReviews(String sortMethod, Integer pageSize, LocalDateTime timestamp);

}
