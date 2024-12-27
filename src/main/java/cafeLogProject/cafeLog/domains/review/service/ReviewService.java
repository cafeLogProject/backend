package cafeLogProject.cafeLog.domains.review.service;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.dto.RegistReviewRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public interface ReviewService {

    @Transactional
    Review addReview(long userId, RegistReviewRequest registReviewRequest);
    Review findReviewById(long reviewId);

}
