package cafeLogProject.cafeLog.api.service;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.domains.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.domains.review.dto.UpdateReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    List<ShowReviewResponse> findReviewListByBeforeCreatedAt(String sortMethod, Integer pageSize, LocalDateTime timestamp);

}
