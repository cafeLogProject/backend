package cafeLogProject.cafeLog.api.review.dto;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ShowReviewResponse {
    private Long reviewId;
    private String content;
    private Integer rating;
    private LocalDate visitDate;
    private List<UUID> imageIds = new ArrayList<>();
    private TagCategory tags;
    private Long cafeId;
    private Long userId;
    private LocalDateTime createdAt;

    @Builder
    public ShowReviewResponse(Long reviewId, String content, Integer rating, LocalDate visitDate, List<UUID> imageIds, TagCategory tags, Long cafeId, Long userId, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = imageIds;
        this.tags = tags;
        this.cafeId = cafeId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // queryDsl에 사용
    @QueryProjection
    public ShowReviewResponse(Review review, List<UUID> imageIds) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.visitDate = review.getVisitDate();
        this.imageIds = imageIds;
        this.tags = new TagCategory(review.getTagIds());
        this.cafeId = review.getCafe().getId();
        this.userId = review.getUser().getId();
        this.createdAt = review.getCreatedAt();
    }


}
