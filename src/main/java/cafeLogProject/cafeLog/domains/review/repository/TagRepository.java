package cafeLogProject.cafeLog.domains.tag.repository;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>{
    List<Tag> findAllByReview(Review review);

}
