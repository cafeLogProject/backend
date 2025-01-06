package cafeLogProject.cafeLog.domains.image.repository;

import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, UUID> {
    ReviewImage save(ReviewImage reviewImage);

    void delete(ReviewImage reviewImage);

    Optional<ReviewImage> findById(UUID uuid);

    //개발 확인용
    List<ReviewImage> findAll();
}
