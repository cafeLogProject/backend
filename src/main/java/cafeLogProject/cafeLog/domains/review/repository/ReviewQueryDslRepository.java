package cafeLogProject.cafeLog.domains.review.repository;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewQueryDslRepository {
    List<Review> findByTagIdsContainingAndBeforeCreatedAt(List<Integer> tagIds, LocalDateTime createdAt);
    List<Review> findReviewsByBeforeCreatedAt(@Param("dateTime") LocalDateTime createdAt);
    List<Review> findByTagIdsContaining(@Param("tags") List<Integer> tagIds);
}
