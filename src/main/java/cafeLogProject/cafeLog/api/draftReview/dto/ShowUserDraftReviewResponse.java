package cafeLogProject.cafeLog.api.draftReview.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Slf4j
public class ShowUserDraftReviewResponse {
    Long draftReviewId;
    LocalDateTime modifiedAt;
    String cafeName;


    @QueryProjection
    public ShowUserDraftReviewResponse(Long draftReviewId, LocalDateTime modifiedAt, String cafeName) {
        this.draftReviewId = draftReviewId;
        this.modifiedAt = modifiedAt;
        this.cafeName = cafeName;
    }
}
