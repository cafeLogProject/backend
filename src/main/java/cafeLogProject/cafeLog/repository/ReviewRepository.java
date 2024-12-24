package cafeLogProject.cafeLog.repository;

import cafeLogProject.cafeLog.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review save(Review review);
    Optional<Review> findById(Long reviewId);
}
