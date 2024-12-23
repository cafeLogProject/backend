package cafeLogProject.cafeLog.repository;

import cafeLogProject.cafeLog.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review save(Review review);
}
