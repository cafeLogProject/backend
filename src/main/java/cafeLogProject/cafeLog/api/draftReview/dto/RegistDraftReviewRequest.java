package cafeLogProject.cafeLog.api.draftReview.dto;

import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.draftReview.domain.DraftReview;
import cafeLogProject.cafeLog.domains.user.domain.User;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class RegistDraftReviewRequest {
    @NotNull(message = "카페ID는 필수 입력 값입니다.")
    private Long cafeId;

    @Builder
    public RegistDraftReviewRequest(Long cafeId) {
        this.cafeId = cafeId;
    }

    public DraftReview toEntity(User user, Cafe cafe) {
        return DraftReview.builder()
                .cafe(cafe)
                .user(user)
                .build();
    }
}

