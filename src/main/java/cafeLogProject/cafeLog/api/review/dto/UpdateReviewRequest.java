package cafeLogProject.cafeLog.api.review.dto;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateReviewRequest {
    private String content;
    @Pattern(regexp="^[1-5]$", message="숫자만 입력할 수 있습니다.")
    private Integer rating;
    private LocalDate visitDate;
    private List<String> imageIds = new ArrayList<>();
    private TagCategory tags;

    @Builder
    public UpdateReviewRequest(String content, int rating, LocalDate visitDate, List<String> imageIds, TagCategory tags) {
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = imageIds;
        this.tags = tags;
    }

    public Review toEntity(Review review) {
        this.content = !(this.content).equals("") ? this.content : review.getContent();
        this.rating = this.rating != null ? this.rating : review.getRating();
        this.visitDate = this.visitDate != null ? this.visitDate : review.getVisitDate();
        this.imageIds = !(this.imageIds).isEmpty() ? this.imageIds : review.getImageIds();
        List<Integer> tagIds = this.tags != null ? this.tags.getAllIds() : review.getTagIds();

        return Review.builder()
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .imageIds(imageIds)
                .tagIds(tagIds)
                .cafe(review.getCafe())
                .user(review.getUser())
                .build();
    }
}
