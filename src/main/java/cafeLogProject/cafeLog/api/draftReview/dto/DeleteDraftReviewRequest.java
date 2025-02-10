package cafeLogProject.cafeLog.api.draftReview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@NoArgsConstructor
@Slf4j
public class DeleteDraftReviewRequest {
    List<Long> draftReviewIds;
}
