package cafeLogProject.cafeLog.domains.review.repository.Impl;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewQueryDslRepository {
    List<Review> findByTagsAndDateTimeOrderByRatingAndDateTime(List<Integer> tagIds, LocalDateTime createdAt, int currentRating);
    List<Review> findByTagsAndDateTimeOrderByDateTime(List<Integer> tagIds, LocalDateTime createdAt);
    List<Review> findBeforeDateTimeOrderByRatingAndDateTime(LocalDateTime createdAt, int currentRating);
    List<Review> findBeforeDateTime(@Param("dateTime") LocalDateTime createdAt);
    List<Review> findByTagsContaining(@Param("tags") List<Integer> tagIds);
}
