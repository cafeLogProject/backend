package cafeLogProject.cafeLog.test.repository;

import cafeLogProject.cafeLog.test.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewEntityRepository extends JpaRepository<ReviewEntity, Long>, ReviewEntityRepositoryCustom {

}
