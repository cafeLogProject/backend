package cafeLogProject.cafeLog.api.draftReview.dto;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import cafeLogProject.cafeLog.domains.draftReview.domain.DraftReview;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Slf4j
public class UpdateDraftReviewRequest {
    private String content;
    @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하여야 합니다.")
    private Integer rating;
    private LocalDate visitDate;
    @Size(max = 5, message = "최대 태그 개수는 5입니다.")
    private List<Integer> tagIds;

    @Builder
    public UpdateDraftReviewRequest(String content, int rating, LocalDate visitDate, List<Integer> tagIds) {
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.tagIds = tagIds;
    }

    public DraftReview toEntity(DraftReview review) {
        log.error(content);
        this.content = this.content != null ? this.content : review.getContent();
        this.rating = this.rating != null ? this.rating : review.getRating();
        this.visitDate = this.visitDate != null ? this.visitDate : review.getVisitDate();
        this.tagIds = this.tagIds != null ? this.tagIds : review.getTagIds();

        return DraftReview.builder()
                .id(review.getId())
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .tagIds(tagIds)
                .imageIds(review.getImageIds())
                .cafe(review.getCafe())
                .user(review.getUser())
                .build();
    }
}

