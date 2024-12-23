package cafeLogProject.cafeLog.service;

import cafeLogProject.cafeLog.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.entity.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface ReviewService {

    @Transactional
    Review addReview(long userId, RegistReviewRequest registReviewRequest);
    Review findReviewById(long reviewId);

}
