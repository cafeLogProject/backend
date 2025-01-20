package cafeLogProject.cafeLog.domains.draftReview.repository;

import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowUserDraftReviewResponse;
import cafeLogProject.cafeLog.domains.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface DraftReviewRepositoryCustom {
    Optional<ShowDraftReviewResponse> findShowDraftReviewResponseById(Long reviewId);
    List<ShowUserDraftReviewResponse> findAllIdsByUser(User user);
}
