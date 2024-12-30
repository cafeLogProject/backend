package cafeLogProject.cafeLog.domains.review.repository;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review save(Review review);
    Optional<Review> findById(Long reviewId);

    // 특정 시간 이전의 데이터 조회
    @Query("SELECT r FROM Review r WHERE r.createdAt < :dateTime")
    Page<Review> findReviewsByBeforeCreatedAt(@Param("dateTime") LocalDateTime localDateTime, Pageable pageable);

    // 특정 태그가 포함된 데이터 조회
    //
}
