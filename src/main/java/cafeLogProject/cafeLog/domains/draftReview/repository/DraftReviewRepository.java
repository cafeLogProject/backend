package cafeLogProject.cafeLog.domains.draftReview.repository;

import cafeLogProject.cafeLog.domains.draftReview.domain.DraftReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftReviewRepository extends JpaRepository<DraftReview, Long>, DraftReviewRepositoryCustom {
}
